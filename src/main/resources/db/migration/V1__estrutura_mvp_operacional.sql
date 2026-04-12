-- V2 - Estrutura MVP operacional
-- Base: empresa, usuário, mesa, categoria, produto, adicional, pedido e pedido_item

-- =========================================================
-- EMPRESA
-- =========================================================
create table if not exists empresa (
    id bigserial primary key,
    nome_fantasia varchar(150) not null,
    razao_social varchar(150),
    cnpj varchar(18) not null unique,
    logo varchar(255),
    status boolean not null default true
);

-- =========================================================
-- USUARIO
-- =========================================================
create table if not exists usuario (
    id bigserial primary key,
    empresa_id bigint not null,
    nome varchar(120) not null,
    cpf varchar(14) not null unique,
    email varchar(150) not null unique,
    telefone varchar(20),
    senha_hash varchar(255) not null,
    perfil varchar(20) not null,
    ativo boolean not null default true,
    ultimo_login timestamp null,

    constraint fk_usuario_empresa
        foreign key (empresa_id) references empresa(id)
);

create index if not exists idx_usuario_empresa on usuario(empresa_id);
create index if not exists idx_usuario_email on usuario(email);
create index if not exists idx_usuario_cpf on usuario(cpf);

-- =========================================================
-- MESA
-- =========================================================
create table if not exists mesa (
    id bigserial primary key,
    empresa_id bigint not null,
    numero integer not null,
    nome varchar(80),
    area varchar(50),
    setor varchar(50),
    andar varchar(50),
    tipo_mesa varchar(50),
    qtd_cadeiras integer,
    posicao_x integer,
    posicao_y integer,
    status varchar(20) not null default 'LIVRE',

    constraint fk_mesa_empresa
        foreign key (empresa_id) references empresa(id)
);

create index if not exists idx_mesa_empresa on mesa(empresa_id);
create index if not exists idx_mesa_empresa_numero on mesa(empresa_id, numero);

-- =========================================================
-- CATEGORIA_PRODUTO
-- =========================================================
create table if not exists categoria_produto (
    id bigserial primary key,
    empresa_id bigint not null,
    nome varchar(100) not null,
    ordem integer not null default 0,
    ativo boolean not null default true,

    constraint fk_categoria_produto_empresa
        foreign key (empresa_id) references empresa(id)
);

create index if not exists idx_categoria_produto_empresa on categoria_produto(empresa_id);

-- =========================================================
-- PRODUTO
-- =========================================================
create table if not exists produto (
    id bigserial primary key,
    empresa_id bigint not null,
    categoria_id bigint not null,
    nome varchar(150) not null,
    descricao varchar(1000),
    preco numeric(10,2) not null,
    foto varchar(255),
    setor_producao varchar(30) not null,
    estoque integer not null default 0,
    ativo boolean not null default true,

    constraint fk_produto_empresa
        foreign key (empresa_id) references empresa(id),

    constraint fk_produto_categoria
        foreign key (categoria_id) references categoria_produto(id)
);

create index if not exists idx_produto_empresa on produto(empresa_id);
create index if not exists idx_produto_categoria on produto(categoria_id);
create index if not exists idx_produto_empresa_ativo on produto(empresa_id, ativo);

-- =========================================================
-- ADICIONAL
-- =========================================================
create table if not exists adicional (
    id bigserial primary key,
    empresa_id bigint not null,
    nome varchar(120) not null,
    preco numeric(10,2) not null default 0,
    qtd_inclusa integer not null default 0,
    qtd_maxima integer not null default 1,
    ativo boolean not null default true,

    constraint fk_adicional_empresa
        foreign key (empresa_id) references empresa(id)
);

create index if not exists idx_adicional_empresa on adicional(empresa_id);

-- =========================================================
-- PEDIDO
-- =========================================================
create table if not exists pedido (
    id bigserial primary key,
    empresa_id bigint not null,
    mesa_id bigint not null,
    usuario_id bigint null,
    garcom_id bigint null,
    status varchar(20) not null default 'RECEBIDO',
    total numeric(10,2) not null default 0,
    tempo_estimado integer,
    criado_em timestamp not null default current_timestamp,

    constraint fk_pedido_empresa
        foreign key (empresa_id) references empresa(id),

    constraint fk_pedido_mesa
        foreign key (mesa_id) references mesa(id),

    constraint fk_pedido_usuario
        foreign key (usuario_id) references usuario(id),

    constraint fk_pedido_garcom
        foreign key (garcom_id) references usuario(id)
);

create index if not exists idx_pedido_empresa on pedido(empresa_id);
create index if not exists idx_pedido_mesa on pedido(mesa_id);
create index if not exists idx_pedido_usuario on pedido(usuario_id);
create index if not exists idx_pedido_status on pedido(status);
create index if not exists idx_pedido_criado_em on pedido(criado_em);

-- =========================================================
-- PEDIDO_ITEM
-- =========================================================
create table if not exists pedido_item (
    id bigserial primary key,
    pedido_id bigint not null,
    produto_id bigint not null,
    quantidade integer not null,
    observacao varchar(500),
    setor varchar(30) not null,
    status varchar(20) not null default 'RECEBIDO',
    subtotal numeric(10,2) not null,

    constraint fk_pedido_item_pedido
        foreign key (pedido_id) references pedido(id) on delete cascade,

    constraint fk_pedido_item_produto
        foreign key (produto_id) references produto(id)
);

create index if not exists idx_pedido_item_pedido on pedido_item(pedido_id);
create index if not exists idx_pedido_item_produto on pedido_item(produto_id);
create index if not exists idx_pedido_item_status on pedido_item(status);
create index if not exists idx_pedido_item_setor on pedido_item(setor);