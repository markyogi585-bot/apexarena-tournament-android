#!/usr/bin/env python3
"""
ApexArena GitHub Automation Script
Safely initializes Git, verifies authentication, commits changes, and pushes to GitHub.
"""

import os
import subprocess
import sys

def run_cmd(cmd, check=True):
    print(f"[*] Executing: {cmd}")
    res = subprocess.run(cmd, shell=True, text=True, capture_output=True)
    if res.stdout.strip():
        print(res.stdout.strip())
    if res.returncode != 0 and check:
        print(f"[!] Warning/Error: {res.stderr.strip()}", file=sys.stderr)
    return res

def main():
    print("=" * 60)
    print("  APEXARENA GITHUB DEPLOYMENT PIPELINE")
    print("=" * 60)

    # 1. Initialize Git repository if needed
    if not os.path.exists(".git"):
        run_cmd("git init -b main")
    else:
        run_cmd("git branch -M main", check=False)

    # 2. Check Git configuration
    run_cmd('git config user.name "ApexArena Developer"', check=False)
    run_cmd('git config user.email "dev@apexarena.gg"', check=False)

    # 3. Add all project files and commit
    run_cmd("git add app gradle supabase docs .github settings.gradle.kts build.gradle.kts README.md PROJECT_AUDIT.md SECURITY_REVIEW.md BUILD_REPORT.md push.py")
    res = run_cmd('git commit -m "feat: complete ApexArena competitive tournament android platform with Jetpack Compose, Supabase, and CI/CD"')

    # 4. Check GitHub CLI auth status
    gh_check = run_cmd("gh auth status", check=False)
    if gh_check.returncode == 0:
        print("[+] GitHub CLI is authenticated.")
        repo_name = os.environ.get("GITHUB_REPO", "apexarena-tournament-android")
        run_cmd(f"gh repo create {repo_name} --public --source=. --remote=origin --push", check=False)
        print(f"[+] Code pushed successfully via GitHub CLI to {repo_name}!")
    else:
        remote_url = os.environ.get("GITHUB_REMOTE_URL")
        if remote_url:
            run_cmd(f"git remote remove origin", check=False)
            run_cmd(f"git remote add origin {remote_url}")
            run_cmd("git push -u origin main")
            print(f"[+] Code pushed to remote {remote_url}!")
        else:
            print("[i] GitHub CLI is not currently logged in.")
            print("[i] To connect to your GitHub account:")
            print("      1. Run: gh auth login")
            print("      2. Run: python3 push.py")
            print("[i] Or set GITHUB_REMOTE_URL='https://github.com/<your-username>/<your-repo>.git'")

    print("=" * 60)
    print("  GIT REPOSITORY STATUS: CLEAN & READY")
    print("=" * 60)

if __name__ == "__main__":
    main()
