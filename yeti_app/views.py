from rest_framework import viewsets

from .serializer import *


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer


class CompetitionViewSet(viewsets.ModelViewSet):
    queryset = Competition.objects.all()
    serializer_class = CompetitionSerializer


class ParticipantViewSet(viewsets.ModelViewSet):
    queryset = Participant.objects.all().order_by('user_id__name')
    serializer_class = ParticipantSerializer


class TeamViewSet(viewsets.ModelViewSet):
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
