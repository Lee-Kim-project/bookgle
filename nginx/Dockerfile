FROM nginx:stable-alpine3.20

COPY ./nginxconfig.conf /etc/nginx/conf.d/nginxconfig.conf
RUN rm /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]