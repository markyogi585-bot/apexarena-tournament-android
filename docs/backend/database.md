# ApexArena Database Architecture & RLS Security

## 1. Overview
ApexArena uses Supabase PostgreSQL 15 with strict relational integrity, versioned migration tracking, and Row Level Security (RLS) on all tables.

## 2. Relational Entity Diagram
- `profiles` (1) <---> (N) `tournament_participants`
- `tournaments` (1) <---> (N) `tournament_participants`
- `tournaments` (1) <---> (N) `matches`
- `teams` (1) <---> (N) `team_members`
- `profiles` (1) <---> (N) `notifications`

## 3. RLS Security Enforcement
- **Public Reads**: Tournaments, Matches, Teams, and Profiles are readable by all authenticated and unauthenticated guests.
- **Strict Writes**:
  - Profile modification is restricted to `auth.uid() = id`.
  - Tournament creation and result updates require organizer permissions (`auth.uid() = organizer_id`).
  - Participant registration enforces capacity limits and unique `(tournament_id, user_id)` constraints to eliminate race conditions.
  - Notifications are strictly locked to `auth.uid() = user_id`.
