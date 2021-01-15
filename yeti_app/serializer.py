from rest_framework import serializers

from .models import *


class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = (
            'id',
            'name',
            'login',
            'password',
            'mail',
            'address',
            'phone',
            'photo',
            'date'
        )


class CompetitionSerializer(serializers.ModelSerializer):

    class Meta:
        model = Competition
        fields = (
            'year',
            'name',
            'address',
            'date',
            'users_number'
        )


class ParticipantSerializer(serializers.ModelSerializer):

    class Meta:
        model = Participant
        fields = (
            'id',
            'user_id',
            'year',
            'score',
            'role',
            'team_id'
        )


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
        fields = (
            'id',
            'team_id',
            'participant_id'
        )


class ImageSerializer(serializers.ModelSerializer):

    class Meta:
        model = Image
        fields = (
            'id',
            'photo',
            'comment',
            'team_id'
        )


class BlueprintSerializer(serializers.ModelSerializer):

    class Meta:
        model = Blueprint
        fields = (
            'id',
            'blueprint',
            'info',
            'team_id'
        )
