# Backend API Integration (IT342)

An Android application demonstrating secure user authentication and database synchronization using **Supabase**, **Retrofit 2**, and **PostgreSQL Triggers**.


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
