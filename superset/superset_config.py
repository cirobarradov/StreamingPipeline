#import os

MAPBOX_API_KEY = 'pk.eyJ1IjoicmljYXJkb2JyYXZvNzkiLCJhIjoiY2poejV0ZTViMDkzZzNwbzF0ZW4xNTFvZCJ9.C00DZSPV6QZZJ9T5oF9pcg'
#MAPBOX_API_KEY = os.getenv('MAPBOX_API_KEY', '')
CACHE_CONFIG = {
    'CACHE_TYPE': 'redis',
    'CACHE_DEFAULT_TIMEOUT': 300,
    'CACHE_KEY_PREFIX': 'superset_',
    'CACHE_REDIS_HOST': 'localhost',
    'CACHE_REDIS_PORT': 6379,
    'CACHE_REDIS_DB': 1,
    'CACHE_REDIS_URL': 'redis://localhost:6379/1'}
SQLALCHEMY_DATABASE_URI = 'sqlite:////var/lib/superset/superset.db'
SQLALCHEMY_TRACK_MODIFICATIONS = True
SECRET_KEY = 'thisISaSECRET_1234'
