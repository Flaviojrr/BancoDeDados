create table alunos
(
    cpf  varchar(11)  not null
        primary key,
    nome varchar(255) not null
);

alter table alunos
    owner to postgres;

create table livros
(
    id             serial
        primary key,
    titulo         varchar(255) not null,
    autor          varchar(255) not null,
    genero         varchar(255) not null,
    editora        varchar(255) not null,
    sinopse        text,
    ano_lancamento integer      not null,
    estado         varchar(20) default 'disponivel'::character varying
);

alter table livros
    owner to postgres;

create table funcionario
(
    cpf   varchar(11) not null
        primary key,
    nome  varchar(255),
    senha varchar(255)
);

alter table funcionario
    owner to postgres;

create table emprestimo
(
    cod_emprestimo  serial
        primary key,
    data_emprestimo date        not null,
    data_devolucao  date,
    cpf_aluno       varchar(11)
        references alunos,
    id_livro        integer
        references livros,
    estado_livro    varchar(20) not null
        constraint emprestimo_estado_livro_check
            check ((estado_livro)::text = ANY
                   ((ARRAY ['emprestado'::character varying, 'disponivel'::character varying])::text[])),
    cpf_funcionario varchar(11)
);

alter table emprestimo
    owner to postgres;



-- Inserindo dados na tabela alunos
INSERT INTO alunos (cpf, nome) VALUES
                                   ('12345678901', 'João Silva'),
                                   ('23456789012', 'Maria Oliveira'),
                                   ('34567890123', 'Carlos Santos'),
                                   ('45678901234', 'Ana Pereira');

-- Inserindo dados na tabela livros
INSERT INTO livros (titulo, autor, genero, editora, sinopse, ano_lancamento, estado) VALUES
                                                                                         ('Aprendendo SQL', 'Alice Rodrigues', 'Tecnologia', 'Editora ABC', 'Um livro sobre SQL', 2020, 'disponivel'),
                                                                                         ('O Mistério do Passado', 'José Lima', 'Mistério', 'Editora XYZ', 'Um thriller emocionante', 2018, 'disponivel'),
                                                                                         ('A Arte da Programação', 'Laura Martins', 'Tecnologia', 'Editora DEF', 'Um guia prático para programadores', 2022, 'disponivel'),
                                                                                         ('História Antiga', 'Ricardo Oliveira', 'História', 'Editora GHI', 'Uma viagem ao passado', 2015, 'disponivel');

-- Inserindo dados na tabela funcionario
INSERT INTO funcionario (cpf, nome, senha) VALUES
                                               ('56789012345', 'Fernanda Santos', '1234'),
                                               ('67890123456', 'Pedro Lima', 'admin01'),
                                               ('78901234567', 'Julia Oliveira', '0000'),
                                               ('89012345678', 'Rafaela Pereira', 'abcd');

-- Inserindo dados na tabela emprestimo
INSERT INTO emprestimo (data_emprestimo, data_devolucao, cpf_aluno, id_livro, estado_livro, cpf_funcionario) VALUES
                                                                                                                 ('2023-01-15', '2023-02-15', '12345678901', 1, 'emprestado', '56789012345'),
                                                                                                                 ('2023-02-01', '2023-03-01', '23456789012', 2, 'emprestado', '67890123456'),
                                                                                                                 ('2023-03-10', '2023-04-10', '34567890123', 3, 'emprestado', '78901234567'),
                                                                                                                 ('2023-04-05', '2023-05-05', '45678901234', 4, 'emprestado', '89012345678');

