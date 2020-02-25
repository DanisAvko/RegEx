package windows

import helpers.RegexHelper
import java.awt.Color
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultHighlighter
import java.io.BufferedInputStream
import javax.swing.JScrollPane

class MainWindow : JFrame() {

    private val textBlock: JEditorPane
    private val btnFind: JButton
    private val btnOpenDialog: JButton

    init {

        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(500, 500)
        textBlock = JEditorPane()
        textBlock.isEditable = false
        btnFind = JButton()
        btnOpenDialog = JButton()
        btnOpenDialog.text = "Открыть файл"
        btnFind.text = "Найти e-mail"
        btnFind.addActionListener { find() }
        val scroll = JScrollPane(
            textBlock,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        )
        btnOpenDialog.addActionListener {
            val filefilter = FileNameExtensionFilter("TXT", "txt")
            val d = JFileChooser()
            d.isAcceptAllFileFilterUsed = false
            d.fileFilter = filefilter
            d.currentDirectory = File(".")
            d.dialogTitle = "Выберите файл"
            d.approveButtonText = "Выбрать"
            d.addChoosableFileFilter(filefilter)
            d.fileSelectionMode = JFileChooser.FILES_ONLY
            val result = d.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                textBlock.text = ""
                val fileInputStream = FileInputStream(d.selectedFile)
                val bufferedInputStream = BufferedInputStream(fileInputStream, 200)
                var i = bufferedInputStream.read()
                do {
                    textBlock.text+=i.toChar()
                    i = bufferedInputStream.read()
                } while (i != -1)



            }
        }

        val gl = GroupLayout(contentPane)
        layout = gl
        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(scroll, 450, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addComponent(
                            btnFind,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addComponent(
                            btnOpenDialog,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )

                )
                .addGap(4)
        )
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(scroll, 400, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
                .addComponent(
                    btnOpenDialog,
                    GroupLayout.PREFERRED_SIZE,
                    GroupLayout.PREFERRED_SIZE,
                    GroupLayout.PREFERRED_SIZE
                )
                .addGap(4)
                .addComponent(
                    btnFind,
                    GroupLayout.PREFERRED_SIZE,
                    GroupLayout.PREFERRED_SIZE,
                    GroupLayout.PREFERRED_SIZE
                )
                .addGap(4)

        )
        pack()
    }

    private fun find() {
        val rh = RegexHelper()
        rh.regex = "([a-z0-9_\\.-]+)@([a-z0-9_\\.-]+)\\.([a-z\\.]{2,6})"
        var txt = textBlock.text
        txt = txt.replace("\r", "")
        val result = rh.findIn(txt)
        val h = textBlock.highlighter
        val hp = DefaultHighlighter
            .DefaultHighlightPainter(Color.YELLOW)
        h.removeAllHighlights()
        for (res in result) {
            try {
                h.addHighlight(res.first, res.second, hp)
            } catch (e: BadLocationException) {
            }
        }
    }
}

