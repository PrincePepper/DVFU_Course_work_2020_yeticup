# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'players.ui'
#
# Created by: PyQt5 UI code generator 5.15.2
#
# WARNING: Any manual changes made to this file will be lost when pyuic5 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt5 import QtCore, QtGui, QtWidgets


class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(926, 508)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setMaximumSize(QtCore.QSize(1080, 800))
        font = QtGui.QFont()
        font.setFamily("Forward")
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        self.centralwidget.setFont(font)
        self.centralwidget.setObjectName("centralwidget")
        self.horizontalLayoutWidget = QtWidgets.QWidget(self.centralwidget)
        self.horizontalLayoutWidget.setGeometry(QtCore.QRect(10, 450, 901, 41))
        self.horizontalLayoutWidget.setObjectName("horizontalLayoutWidget")
        self.horizontalLayout = QtWidgets.QHBoxLayout(self.horizontalLayoutWidget)
        self.horizontalLayout.setContentsMargins(0, 0, 0, 0)
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.findB = QtWidgets.QPushButton(self.horizontalLayoutWidget)
        font = QtGui.QFont()
        font.setFamily("Sans")
        font.setBold(False)
        font.setWeight(50)
        self.findB.setFont(font)
        self.findB.setObjectName("findB")
        self.horizontalLayout.addWidget(self.findB)
        self.updateB = QtWidgets.QPushButton(self.horizontalLayoutWidget)
        font = QtGui.QFont()
        font.setFamily("Sans")
        font.setBold(False)
        font.setWeight(50)
        self.updateB.setFont(font)
        self.updateB.setObjectName("updateB")
        self.horizontalLayout.addWidget(self.updateB)
        self.deleteB = QtWidgets.QPushButton(self.horizontalLayoutWidget)
        font = QtGui.QFont()
        font.setFamily("Sans")
        font.setBold(False)
        font.setWeight(50)
        self.deleteB.setFont(font)
        self.deleteB.setObjectName("deleteB")
        self.horizontalLayout.addWidget(self.deleteB)
        self.addB = QtWidgets.QPushButton(self.horizontalLayoutWidget)
        self.addB.setMinimumSize(QtCore.QSize(100, 0))
        font = QtGui.QFont()
        font.setFamily("Sans")
        font.setBold(False)
        font.setWeight(50)
        self.addB.setFont(font)
        self.addB.setObjectName("addB")
        self.horizontalLayout.addWidget(self.addB)
        self.table = QtWidgets.QTableWidget(self.centralwidget)
        self.table.setGeometry(QtCore.QRect(5, 11, 911, 421))
        self.table.setObjectName("table")
        self.table.setColumnCount(0)
        self.table.setRowCount(0)
        MainWindow.setCentralWidget(self.centralwidget)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "MainWindow"))
        self.findB.setText(_translate("MainWindow", "Добавить"))
        self.updateB.setText(_translate("MainWindow", "Изменить"))
        self.deleteB.setText(_translate("MainWindow", "Удалить"))
        self.addB.setText(_translate("MainWindow", "Дублировать"))