
CREATE TABLE IF NOT EXISTS accounts (
    account_number INTEGER PRIMARY KEY,
    customer_name TEXT NOT NULL,
    age INTEGER NOT NULL,
    balance REAL NOT NULL,
    account_type TEXT NOT NULL,
    status TEXT NOT NULL,
    pin TEXT
);

CREATE TABLE IF NOT EXISTS transactions (
    id TEXT PRIMARY KEY,
    timestamp TEXT NOT NULL,
    account_number INTEGER NOT NULL,
    type TEXT NOT NULL,
    amount REAL NOT NULL,
    balance_after REAL NOT NULL,
    status TEXT NOT NULL,
    remarks TEXT,
    from_account INTEGER DEFAULT 0,
    to_account INTEGER DEFAULT 0
);
