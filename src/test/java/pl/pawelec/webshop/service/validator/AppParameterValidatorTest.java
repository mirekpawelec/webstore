/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import pl.pawelec.webshop.config.WebshopApplication;
import pl.pawelec.webshop.model.AppParameter;
import pl.pawelec.webshop.service.AppParameterService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mirek
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebshopApplication.class})
@SpringBootTest
public class AppParameterValidatorTest {
    @Autowired
    private AppParameterValidator appParameterValidator;
    @Autowired
    private AppParameterService appParameterService;

    public static final String SYMBOL = "test_test";
    public static final String NAME = "test";
    public static final String VALUE = "0";
    public static final String DESCRIPTION = "some test app parameter";

    public static final String SYMBOL_OF_CORRECTLY_CLASS = "correctly_param";
    public static final String NAME_OF_CORRECTLY_CLASS = "parameter";
    public static final String VALUE_OF_CORRECTLY_CLASS = "0";
    public static final String DESCRIPTION_OF_CORRECTLY_CLASS = "some test app parameter";

    @Before
    public void setup() {
        AppParameter testAppParameter = new AppParameter(SYMBOL, NAME, VALUE, DESCRIPTION);
        appParameterService.create(testAppParameter);
    }

    @After
    public void clean() {
        appParameterService.delete(appParameterService.getByUniqueKey(SYMBOL, NAME));
    }

    @Test
    public void a_app_parameter_with_existing_key_should_be_invalid() {
        //given
        AppParameter patternAppParameter = new AppParameter(SYMBOL, NAME, VALUE, DESCRIPTION);
        BindException bindException = new BindException(patternAppParameter, "existingSystemClass");

        //when
        ValidationUtils.invokeValidator(appParameterValidator, patternAppParameter, bindException);

        //then
        assertEquals(1, bindException.getErrorCount());
        assertEquals("symbol", bindException.getFieldError().getField());
        assertTrue(bindException.getFieldError("symbol").getCodes()[0].contains("pl.pawelec.webshop.validator.AppParameterKeyValidator.message"));
    }

    @Test
    public void a_empty_app_parameter_should_throw_3_validation_errors() {
        //given
        AppParameter patternAppParameter = new AppParameter();
        BindException bindException = new BindException(patternAppParameter, "emptySystemClass");

        //when
        ValidationUtils.invokeValidator(appParameterValidator, patternAppParameter, bindException);

        //then
        assertEquals(3, bindException.getErrorCount());
        assertTrue(bindException.getFieldError("value").getDefaultMessage().contains("Wartość jest wymagana"));
        assertTrue(bindException.getFieldError("symbol").getDefaultMessage().contains("Symbol jest wymagany"));
        assertTrue(bindException.getFieldError("name").getDefaultMessage().contains("Nazwa jest wymagana"));
    }

    @Test
    public void a_valid_app_parameter_should_not_be_invalid() {
        //given
        AppParameter patternAppParameter = new AppParameter(SYMBOL_OF_CORRECTLY_CLASS,
                NAME_OF_CORRECTLY_CLASS, VALUE_OF_CORRECTLY_CLASS, DESCRIPTION_OF_CORRECTLY_CLASS);
        BindException bindException = new BindException(patternAppParameter, "correctlySystemClass");

        //when
        ValidationUtils.invokeValidator(appParameterValidator, patternAppParameter, bindException);

        //then
        assertEquals(0, bindException.getErrorCount());
    }
}
