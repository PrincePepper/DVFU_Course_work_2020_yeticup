import sys
from PyQt5 import QtWidgets
import players

class ExampleApp(QtWidgets.QMainWindow, players.Ui_MainWindow):
    def __init__(self):
        super().__init__()
        self.setupUi(self)

def main():
    app = QtWidgets.QApplication(sys.argv)
    window = ExampleApp()
    window.show()
    app.exec_()

if __name__ == '__main__':
    main()