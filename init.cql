CREATE KEYSPACE IF NOT EXISTS url_shortener_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE url_shortener_keyspace;

CREATE TABLE IF NOT EXISTS url_mapping
(
    short_url    TEXT PRIMARY KEY,
    original_url TEXT
);
