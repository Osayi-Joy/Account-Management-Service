CREATE TABLE IF NOT EXISTS auditable (
       id BIGSERIAL PRIMARY KEY,
       created_by VARCHAR(255),
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       last_modified_by VARCHAR(255),
       last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       is_deleted BOOLEAN DEFAULT false
);


CREATE TABLE IF NOT EXISTS permissions (
                             id BIGSERIAL PRIMARY KEY,
                             created_by VARCHAR(255),
                             created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             last_modified_by VARCHAR(255),
                             last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             is_deleted BOOLEAN DEFAULT false,
                             name VARCHAR(255) UNIQUE NOT NULL,
                             description VARCHAR(255),
                             permission_type VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS roles (
                       id BIGSERIAL PRIMARY KEY,
                       created_by VARCHAR(255),
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       last_modified_by VARCHAR(255),
                       last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_deleted BOOLEAN DEFAULT false,
                       name VARCHAR(255) UNIQUE NOT NULL,
                       description VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS roles_and_permissions_mapping (
                                               role_id BIGINT,
                                               permission_id BIGINT,
                                               CONSTRAINT FK_role_permission_role_id FOREIGN KEY (role_id) REFERENCES roles(id),
                                               CONSTRAINT FK_role_permission_permission_id FOREIGN KEY (permission_id) REFERENCES permissions(id),
                                               CONSTRAINT PK_roles_and_permissions_mapping PRIMARY KEY (role_id, permission_id)
);


CREATE TABLE IF NOT EXISTS user_auth_profile (
                                   id BIGSERIAL PRIMARY KEY,
                                   username VARCHAR(255) UNIQUE NOT NULL,
                                   password VARCHAR(255) NOT NULL,
                                   email VARCHAR(255) UNIQUE NOT NULL,
                                   first_name VARCHAR(255),
                                   last_name VARCHAR(255),
                                   phone_number VARCHAR(20),
                                   last_login_date TIMESTAMP,
                                   assigned_role VARCHAR(255),
                                   status VARCHAR(20) DEFAULT 'INACTIVE',
                                   created_by VARCHAR(255),
                                   created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   last_modified_by VARCHAR(255),
                                   last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   is_deleted BOOLEAN DEFAULT false
);


CREATE TABLE IF NOT EXISTS administrators (
                                id BIGSERIAL PRIMARY KEY,
                                user_auth_profile_id BIGINT,
                                created_by VARCHAR(255),
                                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                last_modified_by VARCHAR(255),
                                last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                is_deleted BOOLEAN DEFAULT false,
                                FOREIGN KEY (user_auth_profile_id) REFERENCES user_auth_profile(id)
);

CREATE TABLE IF NOT EXISTS user_permissions_mapping (
                                          id BIGSERIAL PRIMARY KEY,
                                          user_auth_profile_id BIGINT,
                                          permission_id BIGINT,
                                          created_by VARCHAR(255),
                                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          last_modified_by VARCHAR(255),
                                          last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          is_deleted BOOLEAN DEFAULT false,
                                          FOREIGN KEY (user_auth_profile_id) REFERENCES user_auth_profile(id),
                                          FOREIGN KEY (permission_id) REFERENCES permissions(id)
);


CREATE TABLE IF NOT EXISTS administrator_invitation (
                                          id BIGSERIAL PRIMARY KEY,
                                          created_by VARCHAR(255),
                                          created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          last_modified_by VARCHAR(255),
                                          last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          is_deleted BOOLEAN DEFAULT false,
                                          administrator_email VARCHAR(255) UNIQUE NOT NULL,
                                          role VARCHAR(255),
                                          invitation_status VARCHAR(20) DEFAULT 'PENDING',
                                          invitation_code VARCHAR(255) UNIQUE NOT NULL,
                                          deleted BOOLEAN DEFAULT false,
                                          accepted_at TIMESTAMP,
                                          CONSTRAINT CHK_invitation_status CHECK (invitation_status IN ('PENDING', 'ACCEPTED', 'DELETED'))
);


CREATE TABLE IF NOT EXISTS loginAttempts (
                               id BIGSERIAL PRIMARY KEY,
                               created_by VARCHAR(255),
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               last_modified_by VARCHAR(255),
                               last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               is_deleted BOOLEAN DEFAULT false,
                               username VARCHAR(255),
                               failed_attempt_count INT,
                               login_access_denied BOOLEAN,
                               automated_unlock_time TIMESTAMP,
                               profile_type VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS customers (
                           id BIGSERIAL PRIMARY KEY,
                           created_by VARCHAR(255),
                           created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           last_modified_by VARCHAR(255),
                           last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted BOOLEAN DEFAULT false,
                           user_auth_profile_id BIGINT,
                           FOREIGN KEY (user_auth_profile_id) REFERENCES user_auth_profile(id),
                           address VARCHAR(255),
                           date_of_birth DATE,
                           gender VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS documents (
                           id BIGSERIAL PRIMARY KEY,
                           created_by VARCHAR(255),
                           created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           last_modified_by VARCHAR(255),
                           last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           is_deleted BOOLEAN DEFAULT false,
                           customer_id BIGINT,
                           FOREIGN KEY (customer_id) REFERENCES customers(id),
                           type VARCHAR(255),
                           document_number VARCHAR(255),
                           issuer VARCHAR(255),
                           issue_date DATE,
                           expiry_date DATE,
                           file_path VARCHAR(255),
                           description TEXT
);
