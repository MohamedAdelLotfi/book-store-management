# Book Store Management 

Now use `curl` to try this authentication progress.

Sign in via `user/password` pair.

```
curl -X POST http://localhost:9091/auth/login -H "Content-Type:application/json" -d "{\"username\":\"user\", \"password\":\"password\"}"
{
  "username" : "user",
  "token" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2OTQwNDM3NjAsImV4cCI6MTY5NDA0NzM2MH0.K8ru0powTB2d_XWnUL7sMKcbvEQuxRbNfHFdZFvp25A"
}
```

Put the token value to HTTP header `Authorization`, set its value as `Bearer token`, then access the current user info.

```
curl -X GET http://localhost:9091/v1/api/* -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2OTQwNDM3NjAsImV4cCI6MTY5NDA0NzM2MH0.K8ru0powTB2d_XWnUL7sMKcbvEQuxRbNfHFdZFvp25A"
{
  "result...";
}
```