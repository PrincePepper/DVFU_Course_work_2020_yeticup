import random

TABLE_NAME = "teams"

def generate_random_string(length):
    letters = "йцукенгшщзхъфывапролджэячсмитьбю"
    return ''.join(random.choice(letters) for i in range(length))

for i in range(64):
    str1 = generate_random_string(random.randint(4, 16))
    str2 = generate_random_string(random.randint(4, 16))
    print("INSERT INTO " + TABLE_NAME + "(name, score) VALUES(" + '"' + str1 + ' ' + str2 + '"' + ', ' + str(random.randint(4, 128)) + ');')
    