-- =============================================================================
-- ARENA MASTER SCHEMA V2 — COMPLETE COMPETITIVE ESPORTS & REWARDS PLATFORM
-- Version: 20260912000003
-- =============================================================================

-- Enable Cryptographic & UUID Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. PROFILES & GLADIATOR IDENTITY
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

-- 2. TOURNAMENTS
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
    CONSTRAINT check_capacity_bounds CHECK (current_participants <= max_participants)
);

-- 3. TEAMS & ROSTERS
CREATE TABLE IF NOT EXISTS public.teams (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL CHECK (char_length(name) >= 3 AND char_length(name) <= 30),
    tag TEXT UNIQUE NOT NULL CHECK (char_length(tag) >= 2 AND char_length(tag) <= 6),
    leader_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE RESTRICT,
    avatar_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 4. TEAM MEMBERS
CREATE TABLE IF NOT EXISTS public.team_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    team_id UUID NOT NULL REFERENCES public.teams(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    role TEXT NOT NULL DEFAULT 'MEMBER' CHECK (role IN ('LEADER', 'OFFICER', 'MEMBER')),
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    joined_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    CONSTRAINT unique_team_member UNIQUE (team_id, user_id)
);

-- 5. TOURNAMENT PARTICIPANTS
CREATE TABLE IF NOT EXISTS public.tournament_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID NOT NULL REFERENCES public.tournaments(id) ON DELETE CASCADE,
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    team_id UUID REFERENCES public.teams(id) ON DELETE CASCADE,
    slot_number INTEGER NOT NULL DEFAULT 1,
    status TEXT NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('REGISTERED', 'CONFIRMED', 'DISQUALIFIED', 'CHECKED_IN')),
    registered_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    CONSTRAINT unique_solo_participant UNIQUE (tournament_id, user_id)
);

-- 6. MATCH ROOMS & CREDENTIALS (Dispatched 15m prior to start)
CREATE TABLE IF NOT EXISTS public.tournament_rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tournament_id UUID UNIQUE NOT NULL REFERENCES public.tournaments(id) ON DELETE CASCADE,
    room_id TEXT NOT NULL,
    room_password TEXT NOT NULL,
    map_name TEXT NOT NULL DEFAULT 'Bermuda / Erangel',
    is_revealed BOOLEAN NOT NULL DEFAULT FALSE,
    reveal_time TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 7. MATCHES & BRACKET NODES
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

-- 8. FINTECH WALLETS
CREATE TABLE IF NOT EXISTS public.wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    deposit_balance NUMERIC(12,2) NOT NULL DEFAULT 0.00 CHECK (deposit_balance >= 0),
    winning_balance NUMERIC(12,2) NOT NULL DEFAULT 0.00 CHECK (winning_balance >= 0),
    bonus_balance NUMERIC(12,2) NOT NULL DEFAULT 50.00 CHECK (bonus_balance >= 0),
    total_balance NUMERIC(12,2) GENERATED ALWAYS AS (deposit_balance + winning_balance + bonus_balance) STORED,
    kyc_status TEXT NOT NULL DEFAULT 'VERIFIED' CHECK (kyc_status IN ('UNVERIFIED', 'PENDING', 'VERIFIED', 'REJECTED')),
    upi_id TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 9. WALLET TRANSACTIONS (Double-Entry Audit Trail)
CREATE TABLE IF NOT EXISTS public.wallet_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL REFERENCES public.wallets(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    type TEXT NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'ENTRY_FEE', 'PRIZE_PAYOUT', 'BONUS_REWARD', 'REFUND')),
    status TEXT NOT NULL DEFAULT 'SUCCESS' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    reference_id TEXT UNIQUE NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 10. WITHDRAWAL PAYOUT REQUESTS
CREATE TABLE IF NOT EXISTS public.withdrawal_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount >= 50.00),
    upi_id TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'REJECTED')),
    processed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 11. ANNOUNCEMENTS & LIVE BROADCAST DISPATCH
CREATE TABLE IF NOT EXISTS public.announcements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    type TEXT NOT NULL DEFAULT 'SYSTEM' CHECK (type IN ('SYSTEM', 'MATCH', 'ALERT', 'UPDATE')),
    priority TEXT NOT NULL DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'CRITICAL')),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 12. NOTIFICATIONS
CREATE TABLE IF NOT EXISTS public.notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    type TEXT NOT NULL DEFAULT 'SYSTEM' CHECK (type IN ('SYSTEM', 'MATCH', 'TOURNAMENT', 'REWARD', 'WALLET')),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    action_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- =============================================================================
-- PERFORMANCE INDEXES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_tournaments_active ON public.tournaments(status, start_time);
CREATE INDEX IF NOT EXISTS idx_participants_lookup ON public.tournament_participants(tournament_id, user_id);
CREATE INDEX IF NOT EXISTS idx_matches_tourn_round ON public.matches(tournament_id, round_number);
CREATE INDEX IF NOT EXISTS idx_wallets_user ON public.wallets(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON public.wallet_transactions(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_notifications_user_unread ON public.notifications(user_id, is_read);

-- =============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- =============================================================================
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournaments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.teams ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.team_members ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournament_participants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.tournament_rooms ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.matches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.wallets ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.wallet_transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.withdrawal_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.announcements ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;

-- Public Reads
CREATE POLICY "Public Profiles Read" ON public.profiles FOR SELECT USING (true);
CREATE POLICY "Public Tournaments Read" ON public.tournaments FOR SELECT USING (true);
CREATE POLICY "Public Teams Read" ON public.teams FOR SELECT USING (true);
CREATE POLICY "Public Team Members Read" ON public.team_members FOR SELECT USING (true);
CREATE POLICY "Public Participants Read" ON public.tournament_participants FOR SELECT USING (true);
CREATE POLICY "Public Matches Read" ON public.matches FOR SELECT USING (true);
CREATE POLICY "Public Announcements Read" ON public.announcements FOR SELECT USING (is_active = true);

-- Strict Private Reads/Writes
CREATE POLICY "User Profile Update" ON public.profiles FOR UPDATE USING (auth.uid() = id);
CREATE POLICY "User Wallet Read" ON public.wallets FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "User Transactions Read" ON public.wallet_transactions FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "User Withdrawal Insert" ON public.withdrawal_requests FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "User Notifications Read" ON public.notifications FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "User Notifications Update" ON public.notifications FOR UPDATE USING (auth.uid() = user_id);

-- Room Security: Only registered participants can view credentials once revealed
CREATE POLICY "Participant Room Access" ON public.tournament_rooms FOR SELECT USING (
    is_revealed = true AND EXISTS (
        SELECT 1 FROM public.tournament_participants 
        WHERE tournament_participants.tournament_id = tournament_rooms.tournament_id 
        AND tournament_participants.user_id = auth.uid()
    )
);
