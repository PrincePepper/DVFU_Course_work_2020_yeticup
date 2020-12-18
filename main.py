import sqlite3
import sys

from PyQt5 import Qt
from PyQt5 import uic
from PyQt5.QtWidgets import QTableWidgetItem, QDialog, QDialogButtonBox

SELECT_ALL_PLAYERS = "SELECT name, score FROM players"
SELECT_ALL_TEAMS = "SELECT name, score FROM teams"

DELETE_PLAYER = "DELETE FROM players WHERE id = ?"

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
        self.show_players_button.clicked.connect(lambda: playersListWindow.show())
        self.show_teams_button.clicked.connect(lambda: teamsListWindow.show())


class PlayersListWindow(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('players.ui', self)
        self.data = sqlite3.connect('players.db')
        self.command = self.data.cursor()
        self.result = list()
        self.table.setColumnCount(2)
        self.table.setHorizontalHeaderLabels(['Участник', 'Очки'])
        self.table.setRowCount(0)
        self.result = self.command.execute(SELECT_ALL_PLAYERS).fetchall()
        for i, row in enumerate(self.result):
            self.table.setRowCount(self.table.rowCount() + 1)
            for j, elem in enumerate(row):
                self.table.setItem(i, j, QTableWidgetItem(str(elem)))
        self.table.resizeColumnsToContents()
        self.add_button.clicked.connect(lambda: addPlayerDialog.show())
        self.delete_button.clicked.connect(lambda: deletePlayerDialog.show())
        self.duplicate_button.clicked.connect(lambda: duplicatePlayerDialog.show())
        self.data.commit()

    def getGenreName(self, genreNumber):
        for currentGenre in self.genresList:
            if currentGenre[0] == genreNumber:
                return currentGenre[1]
        return False



class TeamsListWindow(Qt.QMainWindow):
    def __init__(self):
        super().__init__()
        uic.loadUi('teams.ui', self)
        self.data = sqlite3.connect('teams.db')
        self.command = self.data.cursor()
        self.result = list()
        self.table.setColumnCount(2)
        self.table.setHorizontalHeaderLabels(['Название', 'Очки'])
        self.table.setRowCount(0)
        self.result = self.command.execute(SELECT_ALL_TEAMS).fetchall()
        for i, row in enumerate(self.result):
            self.table.setRowCount(self.table.rowCount() + 1)
            for j, elem in enumerate(row):
                self.table.setItem(i, j, QTableWidgetItem(str(elem)))
        self.table.resizeColumnsToContents()


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
        # indexes = playersListWindow.table.selectionModel().selectedIndexes()
        # for i in range(len(indexes)):
        #     playersListWindow.command.execute('DELETE from Films WHERE id = ?', (playersListWindow.result[indexes[i].row()][0],))
        #     playersListWindow.table.removeRow(indexes[i].row())
        #     playersListWindow.data.commit()
        # playersListWindow.data.commit()
        self.close()


class DuplicatePlayerDialog(QDialog):
    def __init__(self):
        super().__init__()
        uic.loadUi('duplication.ui', self)


app = Qt.QApplication(sys.argv)
loginWindow = LoginWindow()
loginWindow.show()

mainWindow = Main()

playersListWindow = PlayersListWindow()
teamsListWindow = TeamsListWindow()

addPlayerDialog = AddPlayerDialog()
deletePlayerDialog = DeletePlayerDialog()
duplicatePlayerDialog = DuplicatePlayerDialog()
sys.exit(app.exec_())
