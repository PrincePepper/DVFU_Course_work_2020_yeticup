from rest_framework.decorators import action
from rest_framework.response import Response

from .serializers import *


class UserMixin:
    @action(detail=True, methods=['get'])
    def get_participants(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(user_id=obj.id)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)


class TeamMixin:
    @action(detail=True, methods=['get'])
    def get_requests(self, request, pk=None):
        obj = self.get_object()
        requests = TeamRequest.objects.filter(team_id=obj.id)
        serializer = TeamRequestSerializer(requests, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def get_participants(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(team_id=obj.id)
        if not obj.leader_id in participants:
            print('a')
            leader = Participant.objects.get(id=obj.leader_id.id)
            leader.team_id = obj
            leader.save()
        for p in participants:
            if p.year.year != obj.year:
                p.delete()
        participants = Participant.objects.filter(team_id=obj.id)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def get_images(self):
        obj = self.get_object()
        images = Image.objects.filter(team_id=obj.id)
        serializer = ImageSerializer(images, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def get_blueprints(self):
        obj = self.get_object()
        blueprints = Blueprint.objects.filter(team_id=obj.id)
        serializer = BlueprintSerializer(blueprints, many=True)
        return Response(serializer.data)


class CompetitionMixin:
    @action(detail=True, methods=['get'])
    def get_participants(self, request, pk=None):
        obj = self.get_object()
        participants = Participant.objects.filter(year=obj.year)
        serializer = ParticipantSerializer(participants, many=True)
        return Response(serializer.data)
