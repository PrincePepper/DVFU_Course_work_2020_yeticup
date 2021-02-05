from rest_framework import viewsets

from .mixins import *


class UserViewSet(UserMixin, viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class CompetitionViewSet(CompetitionMixin, viewsets.ModelViewSet):
    queryset = Competition.objects.all().order_by('-date')
    serializer_class = CompetitionSerializer


class ParticipantViewSet(viewsets.ModelViewSet):
    queryset = Participant.objects.all().order_by('user_id__name')
    serializer_class = ParticipantSerializer


class TeamViewSet(TeamMixin, viewsets.ModelViewSet):
    queryset = Team.objects.all().order_by('-leader_id__object_id').order_by(
        '-score')
    serializer_class = TeamSerializer


class RequestViewSet(viewsets.ModelViewSet):
    queryset = TeamRequest.objects.all()
    serializer_class = TeamRequestSerializer


class ImageViewSet(viewsets.ModelViewSet):
    queryset = Image.objects.all()
    serializer_class = ImageSerializer


class BlueprintViewSet(viewsets.ModelViewSet):
    queryset = Blueprint.objects.all()
    serializer_class = BlueprintSerializer
