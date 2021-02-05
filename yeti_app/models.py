from django.db import models
from django.contrib.contenttypes.models import ContentType


class User(models.Model):
    name = models.CharField(max_length=63)
    password = models.CharField(max_length=127)
    mail = models.EmailField(unique=True)
    address = models.CharField(max_length=127)
    phone = models.CharField(max_length=12)
    photo = models.ImageField(upload_to='user_photos', null=True, blank=True)
    date = models.DateField(null=True, blank=True)


class Competition(models.Model):
    year = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=63)
    address = models.CharField(max_length=100)
    date = models.DateField()

    @property
    def total_participants(self):
        return Participant.objects.filter(year=self.year).count()


class Team(models.Model):
    team_name = models.CharField(max_length=25)
    video_path = models.URLField(null=True, blank=True)
    info = models.CharField(max_length=255, null=True, blank=True)
    score = models.IntegerField(default=0)
    leader_id = models.ForeignKey(
        'Participant',
        on_delete=models.CASCADE
    )

    @property
    def get_place(self):
        one_year_team = Team.objects.all().order_by('-score').filter(
            leader_id__year=self.leader_id.year)
        if one_year_team.count() > 0:
            obj = self
            counter = 1
            for team in one_year_team:
                if obj.id == team.id:
                    return counter
                counter += 1
            return counter
        return 0

    place = get_place


class Participant(models.Model):
    roles = (
        ('P', 'participant'),
        ('E', 'expert'),
        ('O', 'organizer')
    )
    user_id = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )

    score = models.IntegerField(default=0)
    role = models.CharField(max_length=1, choices=roles, default='P')
    team_id = models.ForeignKey(
        Team,
        on_delete=models.SET_NULL,
        null=True,
        blank=True
    )
    year = models.ForeignKey(Competition, on_delete=models.CASCADE)


class TeamRequest(models.Model):
    team_id = models.ForeignKey(
        Team,
        on_delete=models.CASCADE
    )
    participant_id = models.ForeignKey(
        Participant,
        on_delete=models.CASCADE
    )


class Image(models.Model):
    photo = models.ImageField(upload_to='team_images')
    comment = models.CharField(max_length=255)
    team_id = models.ForeignKey(
        Team,
        on_delete=models.SET_NULL,
        null=True
    )


class Blueprint(models.Model):
    blueprint = models.ImageField(upload_to='team_blueprints')
    info = models.CharField(max_length=255)
    team_id = models.ForeignKey(
        Team,
        on_delete=models.SET_NULL,
        null=True
    )
