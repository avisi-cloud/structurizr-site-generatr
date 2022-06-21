FROM eclipse-temurin:17-jre

USER root
RUN apt-get update \
    && apt-get install -y graphviz \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
RUN mkdir -p /var/model \
    && chown 65532:65532 /var/model

ENTRYPOINT ["/opt/structurizr-site-generatr/bin/structurizr-site-generatr"]

WORKDIR /opt/structurizr-site-generatr
COPY build/install/structurizr-site-generatr ./
RUN chmod +x /opt/structurizr-site-generatr/bin/structurizr-site-generatr

USER 65532
VOLUME ["/var/model"]
WORKDIR /var/model
