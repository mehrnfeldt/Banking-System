📘 Banking System — Java Backend + Frontend UI
A Java-based banking system that simulates real financial operations including account creation, deposits, withdrawals, transfers, and transaction tracking.
The project includes:
A Java backend (HTTP server)
A frontend UI (TypeScript + HTML/CSS)
A Dockerfile for deployment
A live deployment on Render + Netlify

🚀 Features
🏦 Core Banking Operations
Create new accounts
Deposit funds
Withdraw funds
Transfer between accounts
View transaction history
Persistent state storage (via backend state file)

🌐 Backend API
Lightweight Java HTTP server
REST-style endpoints
JSON request/response format
Deployed on Render

💻 Frontend
Clean UI for interacting with the banking system
Fetches data from the deployed backend
Deployed on Netlify

📂 Project Structure
Code
Banking-System/
│
├── backend/           # Java backend logic
├── frontend/          # UI (TypeScript, HTML, CSS)
├── src/               # Java source (VS Code Java workspace)
├── test/              # Placeholder for JUnit tests
├── Dockerfile         # Backend deployment container
└── README.md          # Project documentation

🔌 API Endpoints
GET /api/accounts
Returns all accounts and balances.

POST /api/accounts
Creates a new account.

POST /api/deposit
Deposits money into an account.

POST /api/withdraw
Withdraws money from an account.

POST /api/transfer
Transfers funds between accounts.

GET /api/state
Returns full backend state (accounts + transactions).

🐳 Running with Docker
Build the image
Code
docker build -t banking-system .
Run the container
Code
docker run -p 8080:8080 banking-system
Backend will be available at:
Code
http://localhost:8080

🧪 Running Locally (Java)
Compile
Code
javac -d bin src/**/*.java
Run
Code
java -cp bin BankHttpServer

🌍 Live Deployments
Backend (Render)
https://banking-system-b2ed.onrender.com/api/state
Frontend (Netlify)
https://mybanksystem1.netlify.app

🛠️ Technologies Used
Java 17
TypeScript
HTML/CSS
Docker
Render (backend hosting)
Netlify (frontend hosting)

👩‍💻 Author
Megan Ehrnfeldt  
GitHub: https://github.com/mehrnfeldt  
Portfolio: https://mehrnfeldt.github.io/portfolio
