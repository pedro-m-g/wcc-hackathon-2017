from django.conf.urls import url, include
from rest_framework.urlpatterns import format_suffix_patterns
from .views import CreateView

urlpatterns = {
    url(r'^screenshot/$', CreateView.as_view(), name="save_screenshot"),
}

urlpatterns = format_suffix_patterns(urlpatterns)