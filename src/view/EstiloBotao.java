package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public final class EstiloBotao {

    private EstiloBotao() {
    }

    public static void aplicarPreenchido(JButton botao, Color fundo, Color texto) {
        botao.setUI(new BasicButtonUI());
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setFocusPainted(false);
        botao.setBackground(fundo);
        botao.setForeground(texto);
        int r = Math.max(0, fundo.getRed() - 35);
        int g = Math.max(0, fundo.getGreen() - 35);
        int b = Math.max(0, fundo.getBlue() - 35);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(r, g, b), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
}
