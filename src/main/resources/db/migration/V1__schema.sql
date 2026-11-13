create table torre (
  id serial primary key,
  numero int not null unique,
  ativa boolean not null default true
);
create table componente (
  id serial primary key,
  nome varchar(60) not null unique,
  ativo boolean not null default true
);
create table sinalizacao (
  id bigserial primary key,
  torre_numero int not null references torre(numero),
  alvo varchar(2) not null,                       -- 'G' (geral) ou número do conjunto
  componente varchar(60),                         -- null = registro sem pendência
  justificativa text not null,
  condicao varchar(1) not null check (condicao in ('D','P','I')),
  situacao varchar(1) not null check (situacao in ('A','E','R','-')),
  fotos int not null default 0,
  registrado_em timestamptz not null default now()
);
create index ix_sinalizacao_alvo on sinalizacao(torre_numero, alvo);
create index ix_sinalizacao_situacao on sinalizacao(situacao);
-- histórico de atendimento: nunca apaga a sinalização original
create table atendimento (
  id bigserial primary key,
  sinalizacao_id bigint not null references sinalizacao(id),
  situacao varchar(1) not null check (situacao in ('A','E','R')),
  responsavel varchar(120),
  descricao text,
  em timestamptz not null default now()
);
create index ix_atendimento_sinalizacao on atendimento(sinalizacao_id);
-- condição operacional atual de cada conjunto / área geral
create table estado_alvo (
  id bigserial primary key,
  torre_numero int not null references torre(numero),
  alvo varchar(2) not null,
  condicao varchar(1) not null check (condicao in ('D','P','I')),
  atualizado_em timestamptz not null,
  unique (torre_numero, alvo)
);
insert into torre(numero) select generate_series(1,12);
insert into componente(nome) values ('Estrutura'),('Motor'),('Rolamento'),('Correia'),('Lubrificação'),('Hélice'),('Cabos'),('Inversor'),('Outros');
