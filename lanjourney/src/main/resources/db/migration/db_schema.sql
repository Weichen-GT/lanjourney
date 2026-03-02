-- =========================
-- 1) users
-- =========================
CREATE TABLE
  IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    display_name TEXT,
    email TEXT UNIQUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    -- backend-controlled "enum"
    auth_provider TEXT NOT NULL, -- e.g. "google"
    auth_provider_user_id TEXT NOT NULL, -- provider unique id (Google "sub")
    avatar_url TEXT,
    locale TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    last_login_at TIMESTAMPTZ,
    CONSTRAINT uq_user_provider_identity UNIQUE (auth_provider, auth_provider_user_id)
  );

-- =========================
-- 2) vocabulary items
-- =========================
CREATE TABLE
  IF NOT EXISTS vocabulary (
    id BIGSERIAL PRIMARY KEY,
    term TEXT NOT NULL, -- word or phrase like "take off"
    is_phrase BOOLEAN NOT NULL DEFAULT FALSE,
    definition_en TEXT,
    -- explanation_zh    TEXT, // Delete to build a separate table for AI-generated content
    part_of_speech TEXT,
    -- example_sentence  TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW ()
  );

CREATE UNIQUE INDEX IF NOT EXISTS uq_vocabulary_term_lower ON vocabulary ((LOWER(term)));

-- =========================
-- 3) user <-> vocabulary relation
-- =========================
CREATE TABLE
  IF NOT EXISTS user_vocabulary (
    user_id BIGINT NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    vocab_id BIGINT NOT NULL REFERENCES vocabulary (id) ON DELETE CASCADE,
    -- backend-controlled "enum"
    status TEXT NOT NULL DEFAULT 'new', -- new/learning/reviewing/mastered
    times_seen INT NOT NULL DEFAULT 0,
    review_count INT NOT NULL DEFAULT 0,
    familiarity_score NUMERIC(5, 2),
    last_seen_at TIMESTAMPTZ,
    last_reviewed_at TIMESTAMPTZ,
    first_added_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    PRIMARY KEY (user_id, vocab_id)
  );

CREATE INDEX IF NOT EXISTS idx_user_vocab_user_status ON user_vocabulary (user_id, status);

CREATE INDEX IF NOT EXISTS idx_user_vocab_vocab ON user_vocabulary (vocab_id);

CREATE TABLE
  IF NOT EXISTS vocab_ai_log (
    id BIGSERIAL PRIMARY KEY,
    term TEXT NOT NULL, -- word or phrase like "take off"
    is_phrase BOOLEAN NOT NULL DEFAULT FALSE,
    definition_en TEXT,
    explanation_zh TEXT,
    part_of_speech TEXT,
    example_sentence TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW ()
  );

CREATE TABLE
  IF NOT EXISTS common_words (
    id BIGSERIAL PRIMARY KEY,
    word TEXT NOT NULL,
    popularity_score INTEGER NOT NULL DEFAULT 0,
    part_of_speech TEXT,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT NOW ()
  );

-- Case-insensitive uniqueness
CREATE UNIQUE INDEX IF NOT EXISTS uq_common_words_word_ci ON common_words (LOWER(word));

-- Helpful index for top-N queries
CREATE INDEX IF NOT EXISTS idx_common_words_popularity ON common_words (popularity_score DESC);

CREATE TABLE
  IF NOT EXISTS vocabulary_zh (
    vocab_id BIGINT PRIMARY KEY REFERENCES vocabulary (id) ON DELETE CASCADE,
    definition_zh TEXT, -- Chinese definition / explanation
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW ()
  );

CREATE TABLE
  IF NOT EXISTS vocab_ds_zh (
    id BIGSERIAL PRIMARY KEY,
    term TEXT NOT NULL,
    translation_zh TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW (),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW ()
  );

-- enforce unique term ignoring case
CREATE UNIQUE INDEX IF NOT EXISTS uq_vocab_datasource_zh_term_ci ON vocab_datasource_zh (LOWER(term));


CREATE TABLE IF NOT EXISTS public.term_status
(
    id bigint NOT NULL DEFAULT nextval('term_status_id_seq'::regclass),
    term text COLLATE pg_catalog."default" NOT NULL,
    has_zh boolean NOT NULL DEFAULT false,
    has_en boolean NOT NULL DEFAULT false,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT term_status_pkey PRIMARY KEY (id)
)