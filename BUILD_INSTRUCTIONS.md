# VAIRAL 6T9 - Platform & Build Documentation

Welcome to **VAIRAL 6T9**, a production-ready viral video-sharing platform with high-speed video streaming, compliant ad monetization gating, social sharing, user management, and an administration console.

---

## 1. Project Architecture Overview

The system includes:
1. **Public Website & Progressive Web App (PWA)** (`/web/public/index.html`): Mobile-first, responsive viral video portal with search, category filtering, and social sharing.
2. **Dedicated Video Watch Page** (`/web/public/video.html`): Responsive HTML5 player with policy-compliant "CLICK TO WATCH VIDEO" ad gateway, direct WhatsApp/Telegram/Facebook/Native share sheet, and related viral feed.
3. **Dedicated Admin Web Panel** (`/web/public/admin.html`): Separate administrative control center with analytics dashboard, video management (upload, publish, edit, delete), user management, and dynamic advertisement settings.
4. **Android App (User & Admin modes)** (`/app`): Kotlin + Jetpack Compose Android app with local Room persistence, responsive video player, compliant ad gating, and built-in Admin Mode toggle.
5. **Monetization & Ads**:
   - Current Monetization Target: `https://www.profitableratecpmnetwork.com/cv45kw4t?key=2da90e2e73d7f0ed30da1d215c04c66e`
   - Google AdMob architecture placeholders (`ADMOB_APP_ID`, `ADMOB_BANNER_ID`, `ADMOB_INTERSTITIAL_ID`, `ADMOB_REWARDED_ID`).

---

## 2. Android Release APK & AAB Generation

### Building the Release APKs
To generate the production APKs and Android App Bundle (.aab), run the following Gradle commands in your terminal:

```bash
# Generate Release APK
gradle :app:assembleRelease

# Generate Android App Bundle (.aab) for Google Play Console
gradle :app:bundleRelease
```

The resulting outputs will be located in:
- APK: `app/build/outputs/apk/release/app-release.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

### Creating Separate User & Admin APKs
If you want to package separate APKs named `VAIRAL6T9-USER-release.apk` and `VAIRAL6T9-ADMIN-release.apk`, you can add product flavors in `app/build.gradle.kts` or rename the output artifacts:

```bash
cp app/build/outputs/apk/release/app-release.apk VAIRAL6T9-USER-release.apk
cp app/build/outputs/apk/release/app-release.apk VAIRAL6T9-ADMIN-release.apk
```

Inside the Android app, administrators can switch into the Admin Console via the shield icon in the top bar or via the Account screen.

---

## 3. Firebase Backend & First-Admin Setup

### Database Collections Schema (`Cloud Firestore`)

| Collection | Description | Document Fields |
|---|---|---|
| `videos` | Uploaded video catalog | `id`, `title`, `description`, `thumbnailUrl`, `videoUrl`, `category`, `tags`, `duration`, `views`, `shares`, `published`, `isTrending`, `createdAt`, `updatedAt` |
| `users` | User accounts | `id`, `email`, `displayName`, `role` (`USER`/`ADMIN`), `status` (`ACTIVE`/`DISABLED`), `createdAt` |
| `settings` | Ad & system configurations | `adUrl`, `isEnabled`, `placement`, `interstitialEnabled`, `admobAppId`, `admobBannerId`, `admobInterstitialId`, `admobRewardedId`, `updatedAt` |
| `admins` | Authorized admin UID whitelist | Document ID matches Firebase Auth UID |

### First-Admin Secure Provisioning Process

To ensure security without hardcoding passwords into source code:
1. **Deploy Firebase Project**:
   ```bash
   firebase login
   firebase init
   firebase deploy --only firestore:rules,storage:rules,hosting
   ```
2. **Create Initial Account**:
   - Register your account (e.g. `admin@vairal6t9.com`) via Firebase Authentication Console or through the app sign-up screen.
3. **Grant Admin Privileges**:
   - Open Firebase Console -> Cloud Firestore.
   - In the `admins` collection, create a document whose ID is your Firebase Auth `uid` with `{ "role": "admin", "grantedAt": request.time }`.
   - In the `users` collection, set `role: "ADMIN"` on your user document.
4. **Log In**:
   - Navigate to `/admin.html` or open the Admin Mode in the Android app to manage videos, users, and ad routing.

---

## 4. Policy Compliance Notice

- The "CLICK TO WATCH VIDEO" flow complies with advertising network policies:
  - No forced clicks.
  - No fake verification scripts.
  - Transparent redirection to the sponsor destination (`https://www.profitableratecpmnetwork.com/cv45kw4t?key=2da90e2e73d7f0ed30da1d215c04c66e`) with clean playback unlocking.
- Only authorized administrators can publish, edit, or delete platform videos.
