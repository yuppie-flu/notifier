CREATE TABLE subscriptions (
  id                uuid PRIMARY KEY,
  utc_delivery_hour smallint NOT NULL,
  enabled           boolean NOT NULL,
  subreddits        text NOT NULL
);

CREATE INDEX ON subscriptions (utc_delivery_hour);

CREATE TABLE users (
  id              uuid PRIMARY KEY,
  name            varchar(50) NOT NULL,
  email           varchar(50) NOT NULL UNIQUE,
  timezone        varchar(50) NOT NULL,
  subscription_id uuid NOT NULL,
  CONSTRAINT fk_subscription
  FOREIGN KEY(subscription_id)
  REFERENCES subscriptions(id)
  ON DELETE CASCADE
);