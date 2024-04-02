CREATE TABLE IF NOT EXISTS account (
    id uuid PRIMARY KEY,
    account_type VARCHAR(255) NOT NULL,
    account_number VARCHAR(255) NOT NULL,
    account_balance MONEY DEFAULT 0,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE OR REPLACE FUNCTION update_updated_at()
    RETURNS TRIGGER AS $$
    BEGIN
        NEW.updated_at = now();
        RETURN NEW;
    END;
    $$ language 'plpgsql';

CREATE TRIGGER update_account_updated_at
    BEFORE UPDATE ON account
    FOR EACH ROW EXECUTE PROCEDURE update_updated_at();