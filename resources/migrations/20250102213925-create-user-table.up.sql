CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- UUID as the primary key with a default value
    email VARCHAR(255) NOT NULL UNIQUE,  -- Email field, unique and not null
    password VARCHAR(255) NOT NULL,  -- Password field, not null
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp for record creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp for record updates
);
