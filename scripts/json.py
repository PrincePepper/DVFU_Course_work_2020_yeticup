import sqlite3
import sys

import requests
import json


# USERS_API_URL = 'https://yetiapi.herokuapp.com/api/users'

# url = 'https://yetiapi.herokuapp.com/api/participants'
# response = requests.get(USERS_API_URL)
# print(response.json())
# names = list()
# scores = list()

# scoreKey = 'score'
# nameKey = 'team_name' if url.split('/')[-1] == 'teams' else 'user_id'

# for key in response.json():
#     for key2 in key:
#         if key2 == nameKey:
#             print((key[key2].split() if url.split('/')[-1] == 'teams' else requests.get(USERS_API_URL).json()[key[key2] - 1]['name']))

#             names.append(key[key2].split() if url.split('/')[-1] == 'teams' else requests.get(USERS_API_URL).json()[key[key2] - 1]['name'])
#         if key2 == scoreKey:
#             print(key[key2])
#             scores.append(key[key2])

# result = list()
# print(len(scores), len(names))
# for i in range(len(names)):
#     result.append(tuple([names[i], scores[i]]))
# print(result)

# result = list(set(result))
# print(result)

# response = requests.get('https://yetiapi.herokuapp.com/api/users/').json()[0]['name']
# print(response)
# logins = list()
# phones = list()
# for key in response.json():
#     for key2 in key:
#         if key2 == 'login':
#             logins += (key[key2].split())
#         if key2 == 'phone':
#             phones += (key[key2].split())
# result = list()
# for i in range(len(logins)):
#     result.append(tuple([logins[i], phones[i]]))
# result = list(set(result))
# print(result)


# print(keys)



# db = sqlite3.connect('../database/players.db')
# command = db.cursor()
# result = command.execute('SELECT name, score FROM players LIMIT 6;').fetchall()
# print(result)

# logins = ['test1', 'test2']
# phones = ['1', '2']

# for i in range(10000):

#     result = list()

#     for i in range(len(logins)):
#         result += (logins[i], phones[i])
    
#     if result[0][-1] != result[0]:
#         print('херня')
#         print(result[0][-1], result[0])
#         break

# print(result)


# url = 'https://yetiapi.herokuapp.com/api/competitions/'
# data = { 'year': 2000, 'name': 'ololo', 'address': 'Olga-City, Big House', 'date': '32', 'users_number': 13}
# response = requests.post(url, data)
# print(response)

# url = 'https://yetiapi.herokuapp.com/api/users/'

# data =  { 'id': 400, 'name': 'teeeeest', 'login': 'olo' + str(400), 'password': '1', 'mail': 'example' + str(400) + '@for.com', 'address': '1', 'phone': '1',
#             'photo': None}
# response = requests.post(url, data)
# print(response.json())

# url = 'https://yetiapi.herokuapp.com/api/users/6'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/7'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/8'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/9'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/10'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/11'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/12'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/13'
# response = requests.delete(url)
# url = 'https://yetiapi.herokuapp.com/api/users/14'
# response = requests.delete(url)
