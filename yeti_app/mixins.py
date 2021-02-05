from rest_framework.decorators import action
from rest_framework.response import Response

from .serializers import *


class UserMixin:
    @action(detail=True, methods=['get'])
    def get_participant(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(user_id=obj.id)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)


class TeamMixin:
    @action(detail=True, methods=['get'])
    def get_request(self, request, pk=None):
        obj = self.get_object()
        requests = TeamRequest.objects.filter(team_id=obj.id)
        serializer = TeamRequestSerializer(requests, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def get_participants(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(team_id=obj.id)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)


class CompetitionMixin:
    @action(detail=True, methods=['get'])
    def get_participant(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(year=obj.year)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)
