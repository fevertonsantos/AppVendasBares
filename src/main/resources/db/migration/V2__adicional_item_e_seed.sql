-- V2 - Join table for pedido_item <-> adicional and seed data

-- =========================================================
-- PEDIDO_ITEM_ADICIONAL (many-to-many)
-- =========================================================
CREATE TABLE IF NOT EXISTS pedido_item_adicional (
    id BIGSERIAL PRIMARY KEY,
    pedido_item_id BIGINT NOT NULL,
    adicional_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL DEFAULT 1,
    preco_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_pia_pedido_item
        FOREIGN KEY (pedido_item_id) REFERENCES pedido_item(id) ON DELETE CASCADE,

    CONSTRAINT fk_pia_adicional
        FOREIGN KEY (adicional_id) REFERENCES adicional(id)
);

CREATE INDEX IF NOT EXISTS idx_pia_pedido_item ON pedido_item_adicional(pedido_item_id);
CREATE INDEX IF NOT EXISTS idx_pia_adicional ON pedido_item_adicional(adicional_id);

-- =========================================================
-- AUDIT LOG
-- =========================================================
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    usuario_id BIGINT,
    acao VARCHAR(100) NOT NULL,
    entidade VARCHAR(100) NOT NULL,
    entidade_id BIGINT,
    detalhes TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_empresa
        FOREIGN KEY (empresa_id) REFERENCES empresa(id),

    CONSTRAINT fk_audit_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE INDEX IF NOT EXISTS idx_audit_empresa ON audit_log(empresa_id);
CREATE INDEX IF NOT EXISTS idx_audit_criado_em ON audit_log(criado_em);

-- =========================================================
-- SEED DATA - Demo establishment
-- =========================================================
INSERT INTO empresa (nome_fantasia, razao_social, cnpj, logo, status)
VALUES ('Bar do Demo', 'Bar do Demo LTDA', '12.345.678/0001-99', NULL, true)
ON CONFLICT (cnpj) DO NOTHING;

-- Categories
INSERT INTO categoria_produto (empresa_id, nome, ordem, ativo) VALUES
(1, 'Comidas', 1, true),
(1, 'Aperitivos', 2, true),
(1, 'Bebidas', 3, true),
(1, 'Drinques', 4, true),
(1, 'Refrigerantes', 5, true),
(1, 'Energeticos', 6, true);

-- Products
INSERT INTO produto (empresa_id, categoria_id, nome, descricao, preco, foto, setor_producao, estoque, ativo) VALUES
(1, 1, 'Picanha na Brasa', 'Picanha grelhada com farofa e vinagrete', 79.90, NULL, 'CHURRASQUEIRA', 50, true),
(1, 1, 'Hamburguer Artesanal', 'Hamburguer 200g com queijo cheddar e bacon', 39.90, NULL, 'COZINHA', 100, true),
(1, 1, 'Porcao de Fritas', 'Batata frita crocante 400g', 29.90, NULL, 'COZINHA', 200, true),
(1, 2, 'Bolinho de Bacalhau', 'Porção com 8 unidades', 34.90, NULL, 'COZINHA', 80, true),
(1, 2, 'Isca de Frango', 'Porção com molho especial', 32.90, NULL, 'COZINHA', 100, true),
(1, 3, 'Cerveja Pilsen 600ml', 'Cerveja gelada long neck', 14.90, NULL, 'BAR', 500, true),
(1, 3, 'Chopp 500ml', 'Chopp artesanal da casa', 12.90, NULL, 'BAR', 300, true),
(1, 4, 'Caipirinha', 'Caipirinha de limão tradicional', 18.90, NULL, 'BAR', 200, true),
(1, 4, 'Mojito', 'Mojito com hortelã fresca', 22.90, NULL, 'BAR', 100, true),
(1, 5, 'Coca-Cola 350ml', 'Refrigerante lata', 7.90, NULL, 'BALCAO', 300, true),
(1, 5, 'Guaraná Antarctica 350ml', 'Refrigerante lata', 7.90, NULL, 'BALCAO', 300, true),
(1, 6, 'Red Bull 250ml', 'Energético lata', 15.90, NULL, 'BALCAO', 100, true);

-- Adicionais
INSERT INTO adicional (empresa_id, nome, preco, qtd_inclusa, qtd_maxima, ativo) VALUES
(1, 'Queijo Extra', 5.00, 0, 3, true),
(1, 'Bacon Extra', 6.00, 0, 2, true),
(1, 'Molho Barbecue', 0.00, 1, 3, true),
(1, 'Molho Mostarda', 0.00, 1, 3, true),
(1, 'Cebola Caramelizada', 4.00, 0, 2, true);

-- Mesas
INSERT INTO mesa (empresa_id, numero, nome, area, setor, andar, tipo_mesa, qtd_cadeiras, posicao_x, posicao_y, status) VALUES
(1, 1, 'Mesa 1', 'Salão Principal', 'Interno', 'Térreo', 'MESA_BAIXA', 4, 100, 100, 'LIVRE'),
(1, 2, 'Mesa 2', 'Salão Principal', 'Interno', 'Térreo', 'MESA_BAIXA', 4, 250, 100, 'LIVRE'),
(1, 3, 'Mesa 3', 'Salão Principal', 'Interno', 'Térreo', 'MESA_BAIXA', 6, 400, 100, 'LIVRE'),
(1, 4, 'Mesa 4', 'Varanda', 'Externo', 'Térreo', 'MESA_ALTA', 2, 100, 300, 'LIVRE'),
(1, 5, 'Mesa 5', 'Varanda', 'Externo', 'Térreo', 'MESA_ALTA', 2, 250, 300, 'LIVRE'),
(1, 6, 'Balcão 1', 'Bar', 'Interno', 'Térreo', 'BALCAO', 3, 100, 500, 'LIVRE');

-- Admin user (password: Admin@123)
INSERT INTO usuario (empresa_id, nome, cpf, email, telefone, senha_hash, perfil, ativo)
VALUES (1, 'Administrador', '000.000.000-00', 'admin@bardodemo.com', '11999999999',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'DONO', true)
ON CONFLICT (email) DO NOTHING;
