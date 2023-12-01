## BRING Basic Context Class Diagram (IoC Container)

```mermaid
classDiagram
    class ApplicationContext {
        - beanDefinitionScanner: BeanDefinitionScanner
        - beanFactory: BeanFactory
        - beanDefinitionRegistry: BeanDefinitionRegistry
        - allowedBeanDefinedAnnotations: Set<Annotation>
        + getBeanFactory(): BeanFactory
    }

    class BeanDefinitionScanner {
        + loadBeanDefinitions(path: String): List
    }

    class BeanDefinitionRegistry {
        - beanDefinitions: Map<String, BeanDefinition>
        + registerBeanDefinition(beanName: String, beanDefinition: BeanDefinition): void
        + getBeanDefinition(beanName: String): BeanDefinition
        + getAllBeanDefinitionNames(): Set<String>
        + getAllBeanDefinitions(): Collection<BeanDefinition>
    }

    class BeanFactory {
        - beanFactoryPostProcessors: List<BeanFactoryPostProcessor>
        - beanPostProcessors: List<BeanPostProcessor>
        - beans: Map<String, Object>
        + setBeanDefinitionRegistry(beanDefinitionRegistry: BeanDefinitionRegistry): void
        + getBean(beanName: String): Object
        + tryToInitializeSingletonBean(beanName: String): Object
        + addBeanFactoryPostProcessor(postProcessor: BeanFactoryPostProcessor): void
        + addBeanPostProcessor(postProcessor: BeanPostProcessor): void
        + getBeanDefinitionByBeanName(beanName: String): BeanDefinition
        + getRegisteredBeanDefinitions(): Collection<BeanDefinition>
        + getAllBeans(): Collection<Object>
    }

    class BeanFactoryPostProcessor {
        + postProcessBeanFactory(beanFactory: BeanFactory): void
    }

    class BeanPostProcessor {
        + postProcessBean(bean: Object, beanName: String): Object
    }

    class Object{
    }

    ApplicationContext -- BeanDefinitionScanner: uses
    ApplicationContext -- BeanFactory: uses
    ApplicationContext -- BeanDefinitionRegistry: uses
    BeanFactory -- BeanFactoryPostProcessor: uses
    BeanFactory -- BeanPostProcessor: uses
    BeanFactory -- Object: creates
    BeanDefinitionScanner -- BeanDefinitionRegistry: uses
    
```

## BRING Initialization Context Sequence Diagram

```mermaid
sequenceDiagram
    participant ApplicationContext as ApplicationContext
    participant BeanDefinitionScanner as BeanDefinitionScanner
    participant BeanDefinitionRegistry as BeanDefinitionRegistry
    participant BeanFactory as BeanFactory
    participant BeanFactoryPostProcessor as BeanFactoryPostProcessor
    participant Object as Object
    participant BeanPostProcessor as BeanPostProcessor


    ApplicationContext->>BeanDefinitionScanner: Use BeanDefinitionScanner
    activate BeanDefinitionScanner
    BeanDefinitionScanner->>BeanDefinitionRegistry: Load BeanDefinitions
    activate BeanDefinitionRegistry
    loop For each Bean Definition
        BeanDefinitionRegistry-->>BeanDefinitionScanner: Register BeanDefinition
    end
    BeanDefinitionRegistry-->>ApplicationContext: BeanDefinitions Loaded
    deactivate BeanDefinitionRegistry
    deactivate BeanDefinitionScanner

    ApplicationContext->>BeanFactory: Create BeanFactory
    activate BeanFactory
    BeanFactory->>BeanFactoryPostProcessor: Use BeanFactoryPostProcessors
    activate BeanFactoryPostProcessor
    BeanFactoryPostProcessor-->>BeanFactory: BeanFactory Configured
    deactivate BeanFactoryPostProcessor

    loop For each Bean Definition in BeanDefinitionRegistry
        BeanFactory->>Object: tryToInitializeSingletonBean(beanName)
        activate Object
        Object-->>BeanFactory: Object Created
        deactivate Object
    end

    BeanFactory->>BeanPostProcessor: Use BeanPostProcessors
    activate BeanPostProcessor
    loop For each Bean
        BeanPostProcessor-->>BeanFactory: Process Bean
    end
    deactivate BeanPostProcessor

    BeanFactory-->>ApplicationContext: Beans Ready
    deactivate BeanFactory
    
```
