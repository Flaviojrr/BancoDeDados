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

