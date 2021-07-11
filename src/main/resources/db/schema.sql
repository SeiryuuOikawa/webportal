/* ユーザマスタ */
CREATE TABLE IF NOT EXISTS m_user (
    user_id VARCHAR(50) PRIMARY KEY,
    encrypted_password VARCHAR(100),
    user_name VARCHAR(50),
    darkmode BOOLEAN,
    role VARCHAR(50),
    enabled BOOLEAN
);

/* タスクテーブル */
CREATE TABLE IF NOT EXISTS task (
  id INT PRIMARY KEY,
  user_id VARCHAR(50),
  comment VARCHAR(200),
  limitday DATE
);