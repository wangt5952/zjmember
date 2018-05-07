/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
    // Define changes to default configuration here.
    // For complete reference see:
    // http://docs.ckeditor.com/#!/api/CKEDITOR.config

    config.language = 'zh-CN'; //语言
    // config.skin = 'v2'; //样式
    config.skin = 'moono-lisa'; //样式
    // config.enterMode = CKEDITOR.ENTER_BR; //回车时产生的标示
    // config.width = '660px'; //默认宽度
    // config.height = '410px'; //默认高度
    config.height = '50em';
    config.resize_enabled = false; //ckedior窗口大小调整功能是否开启
    config.toolbarCanCollapse = false; //工具栏是否可以被收缩

    config.allowedContent = true;
    CKEDITOR.dtd.$removeEmpty.i = 0;
    CKEDITOR.dtd.$removeEmpty['span'] = false;
    config.contentsCss = ['http://cdn.jsdelivr.net/fontawesome/4.5.0/css/font-awesome.min.css','//cdn.jsdelivr.net/bootswatch/3.3.6/readable/bootstrap.css'];

    config.toolbar = 'Basic'; //名字为“Basic”的toolbar（工具栏）的具体设定。只保留以下功能

    config.extraPlugins = 'justify,simage,font,colorbutton,panelbutton';

    // config.imageUploadURL = 'http://121.196.208.176:9002/uploadImage';
    config.imageUploadURL = 'http://121.196.208.176:9000/uploadImage';
    // config.imageUploadURL = 'http://localhost:9000/uploadImage';

    config.dataParser = function(data) {};

    config.colorButton_colors = 'CF5D4E,454545,FFF,CCC,DDD,CCEAEE,66AB16';
    config.colorButton_enableAutomatic = false;

    config.fontSize_sizes = '8/8pt;9/9pt;10/10pt;11/11pt;12/12pt;14/14pt;16/16pt;18/18pt;20/20pt;22/22pt;24/24pt;26/26pt;28/28pt;36/36pt;48/48pt;72/72pt';
    // config.fontSize_sizes = '1em/1em;1.5em/1.5em;2em/2em;2.5em/2.5em;3em/3em;3.5em/3.5em;4em/4em;4.5em/4.5em;5em/5em';
    // config.fontSize_sizes = '1rem/1rem;1.5rem/1.5rem;2rem/2rem;2.5rem/2.5rem;3rem/3rem;3.5rem/3.5rem;4rem/4rem;4.5rem/4.5rem;5rem/5rem';

    config.toolbar_Basic = [
        ['Source', '-', 'Undo', 'Redo', '-', 'Cut', 'Copy', 'Paste', '-', 'Bold', 'Italic', 'Underline', 'Strike', '-', 'NumberedList', 'BulletedList', '-',
            // 'JustifyLeft', 'JustifyRight', 'JustifyCenter', '-', 'SImage', '-', 'FontSize', 'Font', '-', 'Table', 'TextColor', 'BGColor'
            'JustifyLeft', 'JustifyRight', 'JustifyCenter', '-', 'SImage', '-', 'FontSize', '-', 'TextColor', 'BGColor'
        ]
    ];

    config.filebrowserBrowseUrl = '/ckfinder/ckfinder.html'; //上传文件时浏览服务文件夹
    config.filebrowserImageBrowseUrl = '/ckfinder/ckfinder.html?Type=Images'; //上传图片时浏览服务文件夹
    config.filebrowserFlashBrowseUrl = '/ckfinder/ckfinder.html?Type=Flash'; //上传Flash时浏览服务文件夹
    config.filebrowserUploadUrl = '/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Files'; //上传文件按钮(标签)
    config.filebrowserImageUploadUrl = '/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Images'; //上传图片按钮(标签)
    config.filebrowserFlashUploadUrl = '/ckfinder/core/connector/aspx/connector.aspx?command=QuickUpload&type=Flash'; //上传Flash按钮(标签)
    config.font_defaultLabel = "Arial";


    // The toolbar groups arrangement, optimized for two toolbar rows.
    // config.toolbarGroups = [
    //     { name: 'clipboard', groups: ['clipboard', 'undo'] },
    //     { name: 'editing', groups: ['find', 'selection', 'spellchecker'] },
    //     // { name: 'links' },
    //     { name: 'insert' },
    //     { name: 'forms' },
    //     { name: 'tools' },
    //     { name: 'document', groups: ['mode', 'document', 'doctools'] },
    //     { name: 'others' },
    //     { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
    //     { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi'] },
    //     { name: 'styles' },
    //     { name: 'colors' }
    //     // { name: 'about' }
    // ];

    // Remove some buttons provided by the standard plugins, which are
    // not needed in the Standard(s) toolbar.
    // config.removeButtons = 'Underline,Subscript,Superscript';

    // Set the most common block elements.
    config.format_tags = 'p;h1;h2;h3;pre';

    // Simplify the dialog windows.
    config.removeDialogTabs = 'image:advanced;link:advanced';
};