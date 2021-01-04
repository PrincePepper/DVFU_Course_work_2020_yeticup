from rest_framework import routers

from .views import *

router = routers.DefaultRouter()

router.register(r'users', UserViewSet)
router.register(r'competitions', CompetitionViewSet)
router.register(r'participants', ParticipantViewSet)
router.register(r'teams', TeamViewSet)
router.register(r'images', ImageViewSet)
router.register(r'blueprints', BlueprintViewSet)

urlpatterns = router.urls