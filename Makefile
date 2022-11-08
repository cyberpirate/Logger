
build_frontend:
	@echo "Building frontend..."
	@cd frontend && npm run build

clean_web_assets:
	@echo "Cleaning assets..."
	@rm -rfv backend/assets/frontend
	@mkdir backend/assets/frontend

copy_web_assets: clean_web_assets build_frontend
	@echo "Copying assets..."
	@cp -rv frontend/public/* backend/assets/frontend

build_backend: copy_web_assets
	@echo "Building backend..."
	@cd backend && ./gradlew allJars

all: build_backend