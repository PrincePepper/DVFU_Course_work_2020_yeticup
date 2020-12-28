import sqlite3
import sys
from pathlib import Path

from PyQt5.Qt import QMainWindow, QDialog, QApplication
from PyQt5 import uic, QtWidgets
from PyQt5.QtCore import QSize, Qt, QTimer
from PyQt5.QtWidgets import QTableWidgetItem, QDialog, QDialogButtonBox, QDesktopWidget
from PyQt5.QtGui import QKeyEvent, QPixmap

from docx import Document
from docxtpl import DocxTemplate

SELECT_ALL_PLAYERS_FROM_DB = "SELECT name, score FROM players"
SELECT_ALL_TEAMS_FROM_DB = "SELECT name, score FROM teams"
DELETE_PLAYER_FROM_DB = "DELETE FROM players WHERE name = "

DEFAULT_FLAGS = Qt.ItemIsEditable | Qt.ItemIsEnabled

def default_item_constructor(content, flags, _ = None):
    item = QTableWidgetItem(content)
    if flags == DEFAULT_FLAGS:
        item.setFlags(Qt.ItemIsEditable)
        item.setFlags(Qt.ItemIsEnabled)
    else:
        item.setFlags(flags)
    return item

def create_table(table, data, horizontalHeaderLabels, request, item_constructor = default_item_constructor, flags = DEFAULT_FLAGS):
    command = data.cursor()
    table.setColumnCount(len(horizontalHeaderLabels))
    table.setHorizontalHeaderLabels(horizontalHeaderLabels)
    header = table.horizontalHeader()
    header.setSectionResizeMode(0, QtWidgets.QHeaderView.ResizeToContents)
    header.setSectionResizeMode(1, QtWidgets.QHeaderView.Stretch)
    table.setRowCount(0)
    result = command.execute(request).fetchall()
    for i, row in enumerate(result):
        table.setRowCount(table.rowCount() + 1)
        for j, elem in enumerate(row):
            table.setItem(i, j, item_constructor(str(elem), flags, j))
    return result


class LoginWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/login.ui', self)
        self.pushButton.clicked.connect(lambda: self.show_main_window())

    def show_main_window(self):
        mainWindow.showFullScreen()
        self.close()


class Main(QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/main_window.ui', self)
        self.streamOn = False
        self.show_players_button.clicked.connect(lambda: playersListWindow.showFullScreen())
        self.show_teams_button.clicked.connect(lambda: teamsListWindow.showFullScreen())
        self.show_results_button.clicked.connect(lambda: self.show_results())
        self.gen_report_button.clicked.connect(lambda: self.gen_report())
        self.exit_button.clicked.connect(lambda: self.close_all_windows())

    def show_results(self):
        if not self.streamOn:
            stream.show_content()
            monitor = QDesktopWidget().screenGeometry(1)
            stream.move(monitor.left(), monitor.top())
            stream.showFullScreen()
            self.show_results_button.setText('Завершить трансляцию')
        else:
            stream.close()
            self.show_results_button.setText('Транслировать')
        self.streamOn = not self.streamOn

    def gen_report(self):
        competition_name = "Yeti Cup"
        competition_date = "После дождичка в четверг"
        competition_locate = "В большом доме"
        competition_address = "Olga-city"
        manager_name = "Иванов Иван Батькович"

        doc = DocxTemplate("report_template.docx")
        context = { 'competition_name' : competition_name, 'competition_date' : competition_date, 'competition_locate' : competition_locate,
                    'competition_address' : competition_address, 'manager_name' : manager_name }
        doc.render(context)
        doc.save("temp_report.docx")

        doc = Document("temp_report.docx")
        data = sqlite3.connect('database/teams.db')
        command = data.cursor()
        teams = command.execute(SELECT_ALL_TEAMS_FROM_DB).fetchall()
        for i in range(len(teams)):
            doc.tables[0].add_row()
            doc.tables[0].cell(i + 1, 0).text = str(i + 1)
            doc.tables[0].cell(i + 1, 1).text = teams[i][0]
            doc.tables[0].cell(i + 1, 2).text = str(teams[i][1])

        data = sqlite3.connect('database/players.db')
        command = data.cursor()
        players = command.execute(SELECT_ALL_PLAYERS_FROM_DB).fetchall()
        for i in range(len(players)):
            doc.tables[1].add_row()
            doc.tables[1].cell(i + 1, 0).text = str(i + 1)
            doc.tables[1].cell(i + 1, 1).text = str(players[i][1])
            doc.tables[1].cell(i + 1, 2).text = players[i][0]

        doc.save("report.docx")
        Path("temp_report.docx").unlink()

    def close_all_windows(self):
        stream.close()
        playersListWindow.close()
        teamsListWindow.close()
        addPlayerDialog.close()
        deletePlayerDialog.close()
        duplicatePlayerDialog.close()
        mainWindow.close()


class PlayersListWindow(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/players.ui', self)
        self.data = sqlite3.connect('database/players.db')
        self.command = self.data.cursor()
        self.flags = Qt.ItemIsSelectable | Qt.ItemIsEditable | Qt.ItemIsEnabled
        self.result = create_table(self.table, self.data, ['Участник', 'Очки'], SELECT_ALL_PLAYERS_FROM_DB, self.item_constructor, self.flags)

        self.add_button.clicked.connect(lambda: addPlayerDialog.show())
        self.delete_button.clicked.connect(lambda: deletePlayerDialog.show())
        self.duplicate_button.clicked.connect(lambda: duplicatePlayerDialog.show())

        self.save_button.clicked.connect(lambda: self.save())

        self.to_main_window_button.clicked.connect(lambda: self.close())

        self.table.keyPressEvent = self.onKeyPress

    def item_constructor(self, content, flags, n):
        item = QTableWidgetItem(content)
        if n != 0:
            item.setFlags(flags)
        else:
            item.setFlags(Qt.ItemIsEditable)
            item.setFlags(Qt.ItemIsEnabled)
        return item

    def onKeyPress(self, key: QKeyEvent):
        if key.key() == Qt.Key_Return:
            self.save()

    def save(self):
        newTable = []
        oldTable = self.result
        for i in range(len(oldTable)):
            name = self.table.item(i, 0).text()
            score = self.table.item(i, 1).text()
            newTable.append((name, score))
        for i, row in enumerate(newTable):
            if row[1] != oldTable[i][1]:
                self.command.execute('UPDATE players SET score = ' + row[1] + ' WHERE name = "' + row[0] + '"')
                self.data.commit()


class TeamsListWindow(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/teams.ui', self)
        self.data = sqlite3.connect('database/teams.db')
        self.command = self.data.cursor()
        create_table(self.table, self.data, ['Название', 'Очки'], SELECT_ALL_TEAMS_FROM_DB)

        self.to_main_window_button.clicked.connect(lambda: self.close())


class AddPlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/add_player.ui', self)


class DeletePlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/delete_player.ui', self)
        self.buttonBox = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel)
        self.buttonBox.accepted.connect(self.accept)
        self.buttonBox.rejected.connect(self.reject)

    def accept(self):
        rows = playersListWindow.table.selectionModel().selectedIndexes()
        for row in rows:
            playersListWindow.command.execute(DELETE_PLAYER_FROM_DB + '"' + str(playersListWindow.result[row.row()][0]) + '"')
            playersListWindow.table.removeRow(rows[0].row())
            playersListWindow.data.commit()
        self.close()


class DuplicatePlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/duplication.ui', self)


class StreamWindow(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/for_stream.ui', self)
        self.data1 = sqlite3.connect('database/players.db')
        self.data2 = sqlite3.connect('database/teams.db')
        self.label.setPixmap(QPixmap('logo.png'))
        self.label.setAlignment(Qt.AlignCenter)
        self.contents = [self.show_image, self.show_players, self.show_teams]
        self.currentContent = 0

    def show_content(self):
        self.show_image()
        self.contentTimer = QTimer(self, timeout = lambda: self.contents[self.currentContent]())
        self.contentTimer.start(4000)

    def show_image(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.results_table.hide()
        self.label.show()

    def show_players(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.label.hide()
        self.results_table.clear()
        create_table(self.results_table, self.data1, ['Участник', 'Очки'], SELECT_ALL_PLAYERS_FROM_DB)
        self.results_table.show()

    def show_teams(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.label.hide()
        self.results_table.clear()
        create_table(self.results_table, self.data2, ['Название', 'Очки'], SELECT_ALL_TEAMS_FROM_DB)
        self.results_table.show()


app = QApplication(sys.argv)
loginWindow = LoginWindow()
loginWindow.show()

stream = StreamWindow()

mainWindow = Main()

playersListWindow = PlayersListWindow()
teamsListWindow = TeamsListWindow()

addPlayerDialog = AddPlayerDialog()
deletePlayerDialog = DeletePlayerDialog()
duplicatePlayerDialog = DuplicatePlayerDialog()
sys.exit(app.exec_())
