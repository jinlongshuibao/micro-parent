/* 生成静态文档 package com.uiotsoft.micro.common.config;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import springfox.documentation.staticdocs.SwaggerResultHandler;

import com.alibaba.fastjson.JSON;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.user.UserServer;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {UserServer.class})
public class SwaggerStaticDocTest {
    private String snippetDir = "target/generated-snippets";
    private String outputDir  = "target/asciidoc";
    @Autowired
    private MockMvc mockMvc;
    @After
    public void Test() throws Exception {
        // 得到swagger.json,写入outputDir目录中
        mockMvc.perform(get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
                .andDo(SwaggerResultHandler.outputDirectory(outputDir).build())
                .andExpect(status().isOk())
                .andReturn();
        // 读取上一步生成的swagger.json转成asciiDoc,写入到outputDir
        // 这个outputDir必须和插件里面<generated></generated>标签配置一致
        Swagger2MarkupConverter.from(outputDir + "/swagger.json").
                .withPathsGroupedBy(GroupBy.TAGS)// 按tag排序
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)// 格式
                .withExamples(snippetDir)
                .build()
                .intoFolder(outputDir);// 输出

        
		Swagger2MarkupConverter
				.from(Paths.get(outputDir + "/swagger.json").toUri())
				.withConfig(
						new Swagger2MarkupConfigBuilder()
								//.withPathsGroupedBy(GroupBy.TAGS)
								.withMarkupLanguage(MarkupLanguage.ASCIIDOC).withFlatBody().withGeneratedExamples()
								//.withExamples(Paths.get(snippetDir))
								.build()).build()
				.toFolder(Paths.get(outputDir));
		
    }
    
    
    @Test
    public void TestApi() throws Exception {
        mockMvc.perform(get("/authorization" +"/"+ "owener1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("authorization", preprocessResponse(prettyPrint())));
        
        mockMvc.perform(get("/authorization/privilegeTree"+"/" + "role1,role2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("privilegeTree", preprocessResponse(prettyPrint())));
      
        PrivilegeGroupDTO p = new PrivilegeGroupDTO();
        p.setId(1L);
        p.setName("权限组1");
        p.setParentId(null);
        p.setSort(0);
        mockMvc.perform(post("/authorization/privilegeGroup").contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(p))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcRestDocumentation.document("privilegeGroup", preprocessResponse(prettyPrint())));
    }
}
*/