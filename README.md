<img width="1432" height="805" alt="Dashboard" src="https://github.com/user-attachments/assets/e53e5079-5604-4f0d-9dc1-7fbd4088e07d" /># Backend API Integration (IT342)

An Android application demonstrating secure user authentication and database synchronization using **Supabase**, **Retrofit 2**, and **PostgreSQL Triggers**.

Screenshots:
Log In:
<img width="1920" height="1080" alt="ChangePassword" src="https://github.com/user-attachments/assets/76d84db3-f694-42a6-a73b-8029cab01234" />
Register:
<img width="1920" height="1080" alt="Register" src="https://github.com/user-attachments/assets/07b42e3d-4f87-4923-ac9f-905b2895195b" />
Dashboard:
<img width="1432" height="805" alt="Dashboard" src="https://github.com/user-attachments/assets/fc65afc7-a095-462d-bab7-703febc048f5" />
Profile:
<img width="1920" height="1080" alt="Profile" src="https://github.com/user-attachments/assets/f51203ff-61fb-4059-980b-2a4422a25bfa" />

UpdateProfile:
<img width="1920" height="1080" alt="UpdateProfile" src="https://github.com/user-attachments/assets/e4a05548-4c71-4b29-9928-66aa16bd1b1e" />

ChangePassword:
<img width="1920" height="1080" alt="ChangePassword" src="https://github.com/user-attachments/assets/d406d65e-0c92-4bf5-adeb-6e7f68763b36" />

---
To log in use this account:
Username: test@example.com
Password: stormspirit

---

## ✨ Features
* **Signup & Login**: Secure authentication with custom user metadata support.
* **Auto-Sync**: A PostgreSQL Trigger automatically copies new Auth users into the `public.users` database table upon registration.
* **Profile Management**: Update "Full Name" and "Password" directly from the application interface.
* **Networking**: Robust API handling built with **Retrofit 2** and **OkHttp 3** for secure token-based requests.

---

## 🛠 Tech Stack
* **Language**: Kotlin
* **Backend**: Supabase (PostgreSQL & GoTrue Auth)
* **API Architecture**: REST API via Retrofit
* **Networking**: OkHttp with Interceptor for Auth Tokens

---

## 📡 Database Logic
To ensure the Android app can manage user data in a custom table, the following SQL trigger is implemented in the Supabase backend:

```sql
-- Automatically copies new Auth users to the 'public.users' table
create or replace function public.handle_new_user_sync()
returns trigger as $$
begin
  insert into public.users (id, email, full_name)
  values (
    new.id, 
    new.email, 
    new.raw_user_meta_data->>'full_name'
  )
  on conflict (id) do nothing;
  return new;
end;
$$ language plpgsql security definer;
