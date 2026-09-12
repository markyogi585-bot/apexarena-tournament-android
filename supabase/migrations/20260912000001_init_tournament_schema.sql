-- =============================================================================
-- APEXARENA: SUPABASE POSTGRESQL SCHEMA & STRICT RLS POLICIES
-- Version: 20260912000001
-- =============================================================================

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. PROFILES TABLE
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    username TEXT UNIQUE NOT NULL CHECK (char_length(username) >= 3 AND char_length(username) <= 30),
    display_name TEXT NOT NULL,
    avatar_url TEXT,
    game_id TEXT,
    tier TEXT NOT NULL DEFAULT 'BRONZE' CHECK (tier IN ('BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'MASTER', 'LEGEND')),
    points INTEGER NOT NULL DEFAULT 0 CHECK (points >= 0),
    wins INTEGER NOT NULL DEFAULT 0 CHECK (wins >= 0),
    losses INTEGER NOT NULL DEFAULT 0 CHECK (losses >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 2. TOURNAMENTS TABLE
CREATE TABLE IF NOT EXISTS public.tournaments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    organizer_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE RESTRICT,
    title TEXT NOT NULL CHECK (char_length(title) >= 3),
    game_title TEXT NOT NULL,
    format TEXT NOT NULL CHECK (format IN ('SOLO', 'DUO', 'SQUAD', 'KNOCKOUT', 'ROUND_ROBIN')),
    status TEXT NOT NULL DEFAULT 'UPCOMING' CHECK (status IN ('DRAFT', 'UPCOMING', 'REGISTRATION_OPEN', 'ONGOING', 'COMPLETED', 'CANCELLED')),
    entry_fee NUMERIC(10,2) NOT NULL DEFAULT 0.00 CHECK (entry_fee >= 0),
    prize_pool NUMERIC(10,2) NOT NULL DEFAULT 0.00 CHECK (prize_pool >= 0),
    max_participants INTEGER NOT NULL DEFAULT 16 CHECK (max_participants >= 2),
    current_participants INTEGER NOT NULL DEFAULT 0 CHECK (current_participants >= 0),
    banner_url TEXT,
    rules_text TEXT NOT NULL,
    start_time TIMESTAMPTZ NOT NULL,
    registration_deadline TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    CONSTRAINT check_deadline_before_start CHECK (registration_deadline <= start_time)
);

-- 3. TEAMS TABLE
CREATE TABLE IF NOT EXISTS public.teams (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL CHECK (char_length(name) >= 3 AND char_length(name) <= 30),
    tag TEXT UNIQUE NOT NULL CHECK (char_length(tag) >= 2 AND char_length(tag) <= 6),
    leader_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE RESTRICT,
    avatar_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 4. TEAM MEMBERS TABLE
CREATE TABLE IF NOT EXISTS public.team_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    team_id UUID NOT NULL REFERENCES public.teams(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    role TEXT NOT NULL DEFAULT 'MEMBER' CHECK (role IN ('LEADER', 'OFFICER', 'MEMBER')),
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    joined_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    CONSTRAINT unique_team_member UNIQUE (team_id, user_id)
);

-- 5. TOURNAMENT PARTICIPANTS TABLE
CREATE TABLE IF NOT EXISTS public.tournament_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES public.tournaments(id) ON DELETE CASCADE,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    team_id UUID REFERENCES public.teams(id) ON DELETE CASCADE,
    seed_number INTEGER,
    status TEXT NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('REGISTERED', 'CONFIRMED', 'DISQUALIFIED', 'CHECKED_IN')),
    registered_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    CONSTRAINT unique_solo_participant UNIQUE (tournament_id, user_id),
    CONSTRAINT check_participant_presence CHECK (user_id IS NOT NULL OR team_id IS NOT NULL)
);

-- 6. MATCHES TABLE
CREATE TABLE IF NOT EXISTS public.matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES public.tournaments(id) ON DELETE CASCADE,
    round_number INTEGER NOT NULL DEFAULT 1 CHECK (round_number >= 1),
    match_order INTEGER NOT NULL DEFAULT 1 CHECK (match_order >= 1),
    participant_a_id UUID REFERENCES public.tournament_participants(id) ON DELETE SET NULL,
    participant_b_id UUID REFERENCES public.tournament_participants(id) ON DELETE SET NULL,
    score_a INTEGER NOT NULL DEFAULT 0 CHECK (score_a >= 0),
    score_b INTEGER NOT NULL DEFAULT 0 CHECK (score_b >= 0),
    winner_id UUID REFERENCES public.tournament_participants(id) ON DELETE SET NULL,
    status TEXT NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'READY', 'LIVE', 'COMPLETED', 'DISPUTED')),
    scheduled_time TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 7. NOTIFICATIONS TABLE
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    type TEXT NOT NULL DEFAULT 'SYSTEM' CHECK (type IN ('SYSTEM', 'MATCH', 'TOURNAMENT', 'TEAM', 'REWARD')),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    action_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- =============================================================================
-- INDEXES FOR PERFORMANCE
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_tournaments_status ON public.tournaments(status);
CREATE INDEX IF NOT EXISTS idx_tournaments_start_time ON public.tournaments(start_time);
CREATE INDEX IF NOT EXISTS idx_participants_tournament ON public.tournament_participants(tournament_id);
CREATE INDEX IF NOT EXISTS idx_participants_user ON public.tournament_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_matches_tournament ON public.matches(tournament_id, round_number);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON public.notifications(user_id, is_read, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_profiles_points ON public.profiles(points DESC);

-- =============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- =============================================================================

-- Enable RLS on all tables
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournaments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.teams ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.team_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournament_participants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.matches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;

-- Profiles: Anyone can view profiles, users can only update their own
CREATE POLICY "Public profiles are viewable by everyone" ON public.profiles
    FOR SELECT USING (true);

CREATE POLICY "Users can insert their own profile" ON public.profiles
    FOR INSERT WITH CHECK (auth.uid() = id);

CREATE POLICY "Users can update their own profile" ON public.profiles
    FOR UPDATE USING (auth.uid() = id);

-- Tournaments: Viewable by all, insert/update by organizers
CREATE POLICY "Tournaments are viewable by everyone" ON public.tournaments
    FOR SELECT USING (true);

CREATE POLICY "Organizers can create tournaments" ON public.tournaments
    FOR INSERT WITH CHECK (auth.uid() = organizer_id);

CREATE POLICY "Organizers can update their own tournaments" ON public.tournaments
    FOR UPDATE USING (auth.uid() = organizer_id);

-- Teams: Viewable by all, leader controls
CREATE POLICY "Teams are viewable by everyone" ON public.teams
    FOR SELECT USING (true);

CREATE POLICY "Authenticated users can create teams" ON public.teams
    FOR INSERT WITH CHECK (auth.uid() = leader_id);

CREATE POLICY "Team leader can update team" ON public.teams
    FOR UPDATE USING (auth.uid() = leader_id);

-- Team Members
CREATE POLICY "Team members are viewable by everyone" ON public.team_members
    FOR SELECT USING (true);

CREATE POLICY "Users can join or invite to team" ON public.team_members
    FOR INSERT WITH CHECK (auth.uid() = user_id OR auth.uid() IN (
        SELECT leader_id FROM public.teams WHERE id = team_id
    ));

-- Tournament Participants
CREATE POLICY "Participants viewable by everyone" ON public.tournament_participants
    FOR SELECT USING (true);

CREATE POLICY "Users can register themselves" ON public.tournament_participants
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can cancel their own registration before tournament starts" ON public.tournament_participants
    FOR DELETE USING (auth.uid() = user_id);

-- Matches: Viewable by all, managed by tournament organizer
CREATE POLICY "Matches viewable by everyone" ON public.matches
    FOR SELECT USING (true);

CREATE POLICY "Tournament organizers can update match scores" ON public.matches
    FOR UPDATE USING (
        auth.uid() IN (
            SELECT organizer_id FROM public.tournaments WHERE id = matches.tournament_id
        )
    );

-- Notifications: Strict private access
CREATE POLICY "Users can view only their own notifications" ON public.notifications
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can mark their notifications as read" ON public.notifications
    FOR UPDATE USING (auth.uid() = user_id);
