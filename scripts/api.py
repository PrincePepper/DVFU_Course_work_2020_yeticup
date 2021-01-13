import random
import requests
import json

def generate_random_string(length):
    letters = "йцукенгшщзхъфывапролджэячсмитьбю"
    return ''.join(random.choice(letters) for i in range(length))

def generate_random_name():
    return generate_random_string(random.randint(4, 8)) + " " + generate_random_string(random.randint(4, 8))

competitionsUrl = 'https://yetiapi.herokuapp.com/api/competitions/'
competitionsData = { "year": 2000, "name": "ololo", "address": "Olga-City, Big House", "date": "32", "users_number": 101 }
response = requests.post(competitionsUrl, competitionsData)


userUrl = 'https://yetiapi.herokuapp.com/api/users/'
for i in range(1, 101):
    userData = { "id": i, "name": generate_random_name(), "login": "olo" + str(i), "password": "1", "mail": "example" + str(i) + "@for.com", "address": "1", "phone": "1", "photo": None }
    response = requests.post(userUrl, userData)
    print(response.json())


teamsUrl = 'https://yetiapi.herokuapp.com/api/teams/'
teamsData = { "id": 1, "team_name": "Организаторы", "video_path": "._.", "info": "0_o", "place" : -1, "score": -1 }
response = requests.post(teamsUrl, teamsData)
for i in range(2, 27):
    teamsData = { "id": i, "team_name": generate_random_name(), "video_path": "._.", "info": "0_o", "place" : 1, "score": random.randint(0, 128) }
    response = requests.post(teamsUrl, teamsData)
    print(response.json())



playersUrl = 'https://yetiapi.herokuapp.com/api/participants/'
playersData = { "id": 1, "user_id": 1, "year": 2000, "score": -1, "role": "Организатор", "team_id": 1 }
response = requests.post(playersUrl, playersData)
for i in range(2, 101):
    playersData = { "id": i, "user_id": i, "year": 2000, "score": random.randint(0, 128), "role": "Участник", "team_id": (i % 25) + 2 }
    response = requests.post(playersUrl, playersData)
    print(response.json())
