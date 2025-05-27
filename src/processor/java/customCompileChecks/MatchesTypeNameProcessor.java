package customCompileChecks;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SupportedAnnotationTypes("customCompileChecks.MatchesTypeName")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MatchesTypeNameProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, ">>> Processor is running!");
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(MatchesTypeName.class)) {
            if (annotatedElement.getKind() == ElementKind.CLASS) {
                TypeElement clazz = (TypeElement) annotatedElement;
                clazz.getEnclosedElements().stream()
                        .filter(this::isAFieldAnnotatedToIgnoreValidation)
                        .forEach(this::validate);
            }
        }
        return true;
    }

    private void validate(Element annotatedElement) {
        VariableElement field = (VariableElement) annotatedElement;
        String fieldName = field.getSimpleName().toString();
        String typeName = field.asType().toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "typeName :" + typeName);
        @SuppressWarnings("ConstantConditions")
        String simpleTypeName = Pattern.compile("<.*?>").matcher(typeName).find()
                ? extractClassSimpleName(extractBetweenAngles(typeName))
                : extractClassSimpleName(typeName);
        String expectedName = Character.toLowerCase(simpleTypeName.charAt(0)) + simpleTypeName.substring(1);
        if (!fieldName.equals(expectedName)) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Field '" + fieldName + "' must be named '" + expectedName + "' to match its type '" + simpleTypeName + "'",
                    field
            );
        }
    }

    private String extractBetweenAngles(String input) {
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);  // the content between < and >
        }
        return null;  // or "" or throw exception, depending on use case
    }

    private String extractClassSimpleName(String input) {
        return input.substring(input.lastIndexOf('.') + 1);
    }

    private boolean isAFieldAnnotatedToIgnoreValidation(Element element) {
        return element.getKind() == ElementKind.FIELD
                && element.getAnnotation(MatchesTypeName.class) != null
                && !element.getAnnotation(MatchesTypeName.class).ignore();
    }
}
