-- =============================================================================
-- APEXARENA: ADVANCED FINTECH WALLET, UPI TRANSACTIONS & NOTIFICATIONS SCHEMA
-- Version: 20260912000002
-- =============================================================================

-- 8. WALLETS TABLE (Fintech-grade Ledger)
CREATE TABLE IF NOT EXISTS public.wallets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    deposit_balance NUMERIC(12,2) NOT NULL DEFAULT 0.00 CHECK (deposit_balance >= 0),
    winning_balance NUMERIC(12,2) NOT NULL DEFAULT 0.00 CHECK (winning_balance >= 0),
    bonus_balance NUMERIC(12,2) NOT NULL DEFAULT 50.00 CHECK (bonus_balance >= 0), -- Welcome bonus
    total_balance NUMERIC(12,2) GENERATED ALWAYS AS (deposit_balance + winning_balance + bonus_balance) STORED,
    kyc_status TEXT NOT NULL DEFAULT 'UNVERIFIED' CHECK (kyc_status IN ('UNVERIFIED', 'PENDING', 'VERIFIED', 'REJECTED')),
    upi_id TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 9. WALLET TRANSACTIONS (Double-entry Audit Trail)
CREATE TABLE IF NOT EXISTS public.wallet_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    wallet_id UUID NOT NULL REFERENCES public.wallets(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    type TEXT NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'ENTRY_FEE', 'PRIZE_PAYOUT', 'BONUS_REWARD', 'REFUND')),
    status TEXT NOT NULL DEFAULT 'SUCCESS' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED')),
    reference_id TEXT UNIQUE NOT NULL, -- UPI UTR or Order ID
    gateway_name TEXT NOT NULL DEFAULT 'UPI_INSTANT',
    description TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 10. WITHDRAWAL REQUESTS TABLE
CREATE TABLE IF NOT EXISTS public.withdrawal_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    amount NUMERIC(12,2) NOT NULL CHECK (amount >= 50.00), -- Minimum withdrawal ₹50
    upi_id TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'REJECTED')),
    processed_at TIMESTAMPTZ,
    rejection_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- Indexes for lightning queries
CREATE INDEX IF NOT EXISTS idx_wallet_user ON public.wallets(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user ON public.wallet_transactions(user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_withdrawals_user ON public.withdrawal_requests(user_id, status);

-- RLS Security Policies for Wallet
ALTER TABLE public.wallets ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.wallet_transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.withdrawal_requests ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can only view their own wallet" ON public.wallets
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can view only their own transactions" ON public.wallet_transactions
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can create withdrawal requests" ON public.withdrawal_requests
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can view their withdrawal history" ON public.withdrawal_requests
    FOR SELECT USING (auth.uid() = user_id);
