from rest_framework.decorators import action
from rest_framework.response import Response

from .models import Participant

from .serializers import ParticipantSerializer


class UserMixin:
    @action(detail=True, methods=['get'])
    def get_participant(self, request, pk=None):
        obj = self.get_object()
        parts = Participant.objects.filter(user_id=obj.id)
        serializer = ParticipantSerializer(parts, many=True)
        return Response(serializer.data)