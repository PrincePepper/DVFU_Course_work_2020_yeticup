from django.db import models
from django.contrib.contenttypes.fields import GenericForeignKey, \
    GenericRelation
from django.contrib.contenttypes.models import ContentType


class User(models.Model):
    name = models.CharField(max_length=63)
    password = models.CharField(max_length=127)
    mail = models.EmailField(unique=True)
    address = models.CharField(max_length=127)
    phone = models.CharField(max_length=12)
    photo = models.ImageField(upload_to='user_photos', null=True, blank=True)
    date = models.DateTimeField(null=True, blank=True)


class Competition(models.Model):
    year = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=63)
    address = models.CharField(max_length=100)
    date = models.DateTimeField()

    participants = GenericRelation('Participant')

    @property
    def total_participants(self):
        return self.participants.count()


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
            leader_id__object_id=self.leader_id.object_id)
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

    object_id = models.PositiveIntegerField()
    content_type = models.ForeignKey(ContentType, on_delete=models.CASCADE,
                                     default=7)
    content_object = GenericForeignKey('content_type', 'object_id')


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
