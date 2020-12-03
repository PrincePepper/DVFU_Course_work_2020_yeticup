import sqlite3
import sys

from PyQt5 import Qt
from PyQt5 import uic
from PyQt5.QtWidgets import QTableWidgetItem, QDialog, QDialogButtonBox


class LoginWindow(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('login.ui', self)
        self.pushButton.clicked.connect(lambda: self.show_main_window())

    def show_main_window(self):
        mainWindow.show()
        self.close()


class Main(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('main_window.ui', self)
        self.show_players_button.clicked.connect(lambda: playersList.show())
        self.show_teams_button.clicked.connect(lambda: teamsList.show())


class PlayersList(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('players.ui', self)
        self.data = sqlite3.connect('films.db')
        self.command = self.data.cursor()
        self.result = list()
        self.genresList = self.command.execute("SELECT * FROM genres").fetchall()
        self.table.setColumnCount(4)
        self.table.setHorizontalHeaderLabels(['Название', 'Год', 'Жанр', 'Длительность'])
        self.table.setRowCount(0)
        self.result = self.command.execute("SELECT title, year, genre, duration FROM films").fetchmany(100)
        for i, row in enumerate(self.result):
            self.table.setRowCount(self.table.rowCount() + 1)
            for j, elem in enumerate(row):
                self.table.setItem(i, j, QTableWidgetItem(str(self.getGenreName(elem) if j == 2 else elem)))
        self.table.resizeColumnsToContents()
        self.add_button.clicked.connect(lambda: addPlayerDialog.show())
        self.delete_button.clicked.connect(lambda: deletePlayerDialog.show())
        self.duplicate_button.clicked.connect(lambda: duplicatePlayerDialog.show())

    def getGenreName(self, genreNumber):
        for currentGenre in self.genresList:
            if currentGenre[0] == genreNumber:
                return currentGenre[1]
        return False



class TeamsList(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('teams.ui', self)
        self.data = sqlite3.connect('films.db')
        self.command = self.data.cursor()
        self.result = list()
        self.genresList = self.command.execute("SELECT * FROM genres").fetchall()
        self.table.setColumnCount(4)
        self.table.setHorizontalHeaderLabels(['Название', 'Год', 'Жанр', 'Длительность'])
        self.table.setRowCount(0)
        self.result = self.command.execute("SELECT title, year, genre, duration FROM films").fetchall()
        for i, row in enumerate(self.result):
            self.table.setRowCount(self.table.rowCount() + 1)
            for j, elem in enumerate(row):
                self.table.setItem(i, j, QTableWidgetItem(str(self.getGenreName(elem) if j == 2 else elem)))
        self.table.resizeColumnsToContents()

    def getGenreName(self, genreNumber):
        for currentGenre in self.genresList:
            if currentGenre[0] == genreNumber:
                return currentGenre[1]
        return False


class AddPlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('add_player.ui', self)


class DeletePlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('delete_player.ui', self)
        self.buttonBox = QDialogButtonBox(QDialogButtonBox.Ok | QDialogButtonBox.Cancel)
        self.buttonBox.accepted.connect(self.accept)
        self.buttonBox.rejected.connect(self.reject)

    def accept(self):
        # indexes = window.table.selectionModel().selectedIndexes()
        # for i in range(len(indexes)):
        #     window.command.execute('DELETE from Films WHERE id = ?', (window.result[indexes[i].row()][0],))
        #     window.table.removeRow(indexes[i].row())
        # window.data.commit()
        self.close()


class DuplicatePlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('duplication.ui', self)


app = Qt.QApplication(sys.argv)
login = LoginWindow()
login.show()

mainWindow = Main()

playersList = PlayersList()
teamsList = TeamsList()

addPlayerDialog = AddPlayerDialog()
deletePlayerDialog = DeletePlayerDialog()
duplicatePlayerDialog = DuplicatePlayerDialog()
sys.exit(app.exec_())
