import sys
from pathlib import Path

import requests
import json

from PyQt5.Qt import QMainWindow, QDialog, QApplication
from PyQt5 import uic, QtWidgets
from PyQt5.QtCore import QSize, Qt, QTimer
from PyQt5.QtWidgets import QLineEdit, QTableWidgetItem, QDialog, QDialogButtonBox, QDesktopWidget
from PyQt5.QtGui import QKeyEvent, QPixmap

from docx import Document
from docxtpl import DocxTemplate

competitionName_ = ''
login_ = ''
password_ = ''

PLAYERS_API_URL = 'https://yetiapi.herokuapp.com/api/participants'
TEAMS_API_URL = 'https://yetiapi.herokuapp.com/api/teams'
USERS_API_URL = 'https://yetiapi.herokuapp.com/api/users'
COMPETITIONS_API_URL = 'https://yetiapi.herokuapp.com/api/competitions'

PLAYERS_API_RESPONSE = requests.get(PLAYERS_API_URL).json()
TEAMS_API_RESPONSE = requests.get(TEAMS_API_URL).json()
USERS_API_RESPONSE = requests.get(USERS_API_URL).json()
COMPETITIONS_API_RESPONSE = requests.get(COMPETITIONS_API_URL).json()

DELETE_PLAYER_FROM_DB = 'DELETE FROM players WHERE name = '

DEFAULT_FLAGS = Qt.ItemIsEditable | Qt.ItemIsEnabled

def default_item_constructor(content, flags, _ = None):
    item = QTableWidgetItem(content)
    if flags == DEFAULT_FLAGS:
        item.setFlags(Qt.ItemIsEditable)
        item.setFlags(Qt.ItemIsEnabled)
    else:
        item.setFlags(flags)
    return item

def get_result(url):
    if url.split('/')[-1] == 'teams':
        response = TEAMS_API_RESPONSE
    if url.split('/')[-1] == 'participants':
        response = PLAYERS_API_RESPONSE

    names = list()
    scores = list()

    scoreKey = 'score'
    nameKey = 'team_name' if url.split('/')[-1] == 'teams' else 'user_id'

    for key in response:
        for key2 in key:
            if key2 == nameKey:
                # names.append((*key[key2].split()) if url.split('/')[-1] == 'teams' else USERS_API_RESPONSE[key[key2] - 1]['name'])
                if url.split('/')[-1] == 'teams':
                    names.append((*key[key2].split()))
                else:
                    names.append(USERS_API_RESPONSE[key[key2] - 1]['name'])
            if key2 == scoreKey:
                scores.append(key[key2])

    result = list()
    for i in range(len(names)):
        if scores[i] >= 0:
            result.append(tuple([names[i], scores[i]]))

    return result

def create_table(table, horizontalHeaderLabels, url, item_constructor = default_item_constructor, flags = DEFAULT_FLAGS):
    table.setColumnCount(len(horizontalHeaderLabels))
    table.setHorizontalHeaderLabels(horizontalHeaderLabels)
    header = table.horizontalHeader()
    header.setSectionResizeMode(0, QtWidgets.QHeaderView.ResizeToContents)
    header.setSectionResizeMode(1, QtWidgets.QHeaderView.Stretch)
    table.setRowCount(0)

    result = get_result(url)

    for i, row in enumerate(result):
        table.setRowCount(table.rowCount() + 1)
        for j, elem in enumerate(row):
            table.setItem(i, j, item_constructor(str(elem), flags, j))
    return result


class LoginWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/login.ui', self)
        self.password.setEchoMode(QLineEdit.Password)
        self.pushButton.clicked.connect(lambda: self.show_main_window())

    def show_main_window(self):
        competition_name_ = self.competition_name.text()
        login_ = self.login.text()
        password_ = self.password.text()

        year = [competition['year'] for competition in COMPETITIONS_API_RESPONSE if competition['name'] == competition_name_]

        role = [player['role'] for user in USERS_API_RESPONSE if user['login'] == login_ for player in PLAYERS_API_RESPONSE if user['id'] == player['user_id'] and player['role']]

        password = [user['password'] for user in USERS_API_RESPONSE if user['password'] == password_]

        if year and role and password:
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
            stream.contentTimer.stop()
            self.show_results_button.setText('Транслировать')
        self.streamOn = not self.streamOn

    def gen_report_table(self, doc, n, url):
        result = get_result(url)

        for i in range(len(result)):
            doc.tables[n].add_row()
            doc.tables[n].cell(i + 1, 0).text = str(i + 1)
            doc.tables[n].cell(i + 1, 1).text = str(result[i][1])
            doc.tables[n].cell(i + 1, 2).text = result[i][0]

    def gen_report(self):
        competition_name = competition_name_
        competition_date = 'После дождичка в четверг'
        competition_address = 'Olga-city, Big House'
        manager_name = 'Иванов Иван Батькович'

        doc = DocxTemplate('report_template.docx')
        context = { 'competition_name' : competition_name, 'competition_date' : competition_date,
                    'competition_address' : competition_address, 'manager_name' : manager_name }
        doc.render(context)
        doc.save('temp_report.docx')

        doc = Document('temp_report.docx')

        self.gen_report_table(doc, 0, TEAMS_API_URL)
        self.gen_report_table(doc, 1, PLAYERS_API_URL)

        doc.save('report.docx')
        Path('temp_report.docx').unlink()

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
        self.flags = Qt.ItemIsSelectable | Qt.ItemIsEditable | Qt.ItemIsEnabled
        self.result = create_table(self.table, ['Участник', 'Очки'], PLAYERS_API_URL, self.item_constructor, self.flags)

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
                self.command.execute('UPDATE players SET score = ' + row[1] + ' WHERE name = '' + row[0] + ''')
                self.data.commit()


class TeamsListWindow(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('ui_files/teams.ui', self)
        create_table(self.table, ['Название', 'Очки'], TEAMS_API_URL)

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
            playersListWindow.command.execute(DELETE_PLAYER_FROM_DB + ''' + str(playersListWindow.result[row.row()][0]) + ''')
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
        self.label.setPixmap(QPixmap('logo.png'))
        self.label.setAlignment(Qt.AlignCenter)
        self.contents = [self.show_image, self.show_players, self.show_teams]
        self.currentContent = 0
        self.contentTimer = QTimer(self, timeout = lambda: self.contents[self.currentContent]())

    def show_content(self):
        self.show_image()
        self.contentTimer.start(4000)

    def show_image(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.results_table.hide()
        self.label.show()

    def show_players(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.label.hide()
        self.results_table.clear()
        create_table(self.results_table, ['Участник', 'Очки'], PLAYERS_API_URL)
        self.results_table.show()

    def show_teams(self):
        self.currentContent = (self.currentContent + 1) % len(self.contents)
        self.label.hide()
        self.results_table.clear()
        create_table(self.results_table, ['Название', 'Очки'], TEAMS_API_URL)
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
