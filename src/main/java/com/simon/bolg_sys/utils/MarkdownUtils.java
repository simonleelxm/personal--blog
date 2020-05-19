package com.simon.bolg_sys.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;


public class MarkdownUtils {

    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }


    public static void main(String[] args) {
        String table = "# 小米商城项目\n" +
                "\n" +
                "### 实体类\n" +
                "\n" +
                "- 用户类、用户分页类\n" +
                "- 商品类、商品分页类\n" +
                "- 商品种类类\n" +
                "- 订单类\n" +
                "- 购物车类\n" +
                "\n" +
                "### 系统功能\n" +
                "\n" +
                "##### 后台\n" +
                "\n" +
                "1. 后台登录页：前端验证、后台数据库验证、记住用户名功能（待优化）、忘记密码（待优化）\n" +
                "2. 后台主页：\n" +
                "   - 注销系统\n" +
                "\n" +
                "   - 上次登录时间（待优化）\n" +
                "   - 用户管理：分页查询、批量删除用户、编辑用户权限\n" +
                "   - 商品类别管理：删除、修改、添加商品类别信息、分页（待优化）、修改时逻辑判断（待优化）\n" +
                "   - 商品管理：分页查询、批量删除、添加商品信息、分页模糊查询\n" +
                "   - 订单管理（待优化）：CRUD、订单状态（待优化）\n" +
                "\n" +
                "##### 前台\n" +
                "\n" +
                "1. 前台登录页：数据库验证手机号、手机短信验证\n" +
                "2. 前台注册页：前端验证、ajax验证、数据库验证、图片上传\n" +
                "3. 前台主页：动态加载页面、cookie存储（搜索历史）、当前登录用户、退出登录、用户猜你喜欢推荐功能（待优化）\n" +
                "4. 商品详情页：动态加载商品详情、登录判断、加入购物车\n" +
                "5. 购物车页：动态加载页面、实时加减商品数量实时修改总价（待优化）\n" +
                "6. 虚拟支付功能、支付完成跳转页面（待优化）\n" +
                "\n" +
                "##### 总系统\n" +
                "\n" +
                "1. 全局拦截（待优化）\n" +
                "2. 缓存问题``getLastModified()``（待优化）";
        String a = "[imCoding 爱编程](http://www.lirenmi.cn)";
        System.out.println(markdownToHtmlExtensions(table));
    }
}
