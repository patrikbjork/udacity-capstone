FROM nginx:alpine
RUN echo "Copying client..."
COPY /dist/client /usr/share/nginx/html
RUN echo "Finished copying client..."
COPY docker/default.conf /etc/nginx/conf.d/
EXPOSE 80
