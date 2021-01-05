from django.db import models


class User(models.Model):
    name = models.CharField(max_length=63)
    login = models.CharField(max_length=63, unique=True)
    password = models.CharField(max_length=127)
    mail = models.CharField(max_length=255, unique=True)
    address = models.CharField(max_length=127)
    phone = models.CharField(max_length=15, unique=True)
    photo = models.ImageField(upload_to='user_photos')
    date = models.DateTimeField(auto_now_add=True)


class Competition(models.Model):
    year = models.IntegerField(primary_key=True)
    address = models.CharField(max_length=100)
    date = models.DateTimeField(auto_now_add=True, unique=True)
    users_number = models.IntegerField()


class Image(models.Model):
    photo = models.ImageField(upload_to='team_images')
    comment = models.CharField(max_length=255)


class Blueprint(models.Model):
    blueprint = models.ImageField(upload_to='team_blueprints')
    info = models.CharField(max_length=255)


class Team(models.Model):
    team_name = models.CharField(max_length=25)
    video_path = models.CharField(max_length=255)
    info = models.CharField(max_length=255)
    place = models.IntegerField()
    image_id = models.ForeignKey(
        Image,
        on_delete=models.CASCADE
    )
    blueprint_id = models.ForeignKey(
        Blueprint,
        on_delete=models.CASCADE
    )


class Participant(models.Model):
    user_id = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )
    year = models.ForeignKey(
        Competition,
        on_delete=models.CASCADE
    )
    score = models.IntegerField()
    role = models.CharField(max_length=20)
    team_id = models.ForeignKey(
        Team,
        on_delete=models.SET_NULL,
        null=True
    )
