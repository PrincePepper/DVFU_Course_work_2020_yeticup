from rest_framework import serializers

from .models import *


class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = '__all__'


class CompetitionSerializer(serializers.ModelSerializer):

    class Meta:
        model = Competition
        fields = (
            'year',
            'name',
            'address',
            'date',
            'total_participants'
        )


class ParticipantSerializer(serializers.ModelSerializer):

    class Meta:
        model = Participant
        fields = '__all__'


class TeamSerializer(serializers.ModelSerializer):

    class Meta:
        model = Team
        fields = (
            'id',
            'team_name',
            'video_path',
            'info',
            'score',
            'place',
            'leader_id'
        )


class TeamRequestSerializer(serializers.ModelSerializer):

    class Meta:
        model = TeamRequest
        fields = '__all__'


class ImageSerializer(serializers.ModelSerializer):

    class Meta:
        model = Image
        fields = '__all__'


class BlueprintSerializer(serializers.ModelSerializer):

    class Meta:
        model = Blueprint
        fields = '__all__'
