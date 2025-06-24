DROP DATABASE IF EXISTS sistema_consultas;

CREATE DATABASE sistema_consultas;
USE sistema_consultas;

CREATE TABLE admin (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       nome VARCHAR(255) NOT NULL,
                       senha VARCHAR(255) NOT NULL
);

CREATE TABLE medico (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        nome VARCHAR(255) NOT NULL,
                        senha VARCHAR(255) NOT NULL,
                        especialidade VARCHAR(255) NOT NULL
);

CREATE TABLE paciente (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          nome VARCHAR(255) NOT NULL,
                          senha VARCHAR(255) NOT NULL
);

CREATE TABLE agendamento_consulta (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      data DATE NOT NULL,
                                      hora TIME NOT NULL,
                                      status VARCHAR(255) NOT NULL,
                                      medico_id BIGINT NOT NULL,
                                      paciente_id BIGINT NOT NULL,
                                      FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE,
                                      FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE CASCADE
);

CREATE TABLE consulta (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          data DATE NOT NULL,
                          hora TIME NOT NULL,
                          medico_id BIGINT NOT NULL,
                          paciente_id BIGINT NOT NULL,
                          FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE,
                          FOREIGN KEY (paciente_id) REFERENCES paciente(id) ON DELETE CASCADE
);

CREATE TABLE diagnostico (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             descricao VARCHAR(255) NOT NULL,
                             consulta_id BIGINT NOT NULL,
                             FOREIGN KEY (consulta_id) REFERENCES consulta(id) ON DELETE CASCADE
);

CREATE TABLE prescricao (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            data_inicio DATE NOT NULL,
                            data_fim DATE NOT NULL,
                            dosagem VARCHAR(255) NOT NULL,
                            frequencia VARCHAR(255) NOT NULL,
                            medicamento VARCHAR(255) NOT NULL,
                            diagnostico_id BIGINT NOT NULL,
                            FOREIGN KEY (diagnostico_id) REFERENCES diagnostico(id) ON DELETE CASCADE
);
